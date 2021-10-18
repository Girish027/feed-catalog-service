package feed.catalog.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feed.catalog.api.response.dto.DestinationDTO;
import feed.catalog.api.response.dto.FeedCreateDTO;
import feed.catalog.api.response.dto.FeedUpdateDTO;
import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.api.response.handler.HandlerResponse;
import feed.catalog.api.response.utils.validator.annotations.implementations.DTOModelMapperImpl;
import feed.catalog.dao.DestinationDao;
import feed.catalog.dao.FeedDao;
import feed.catalog.domain.Destination;
import feed.catalog.domain.Feed;
import feed.catalog.api.response.dto.FeedSnapShot;
import feed.catalog.queue.Producer;
import feed.catalog.rest.RestClient;
import feed.catalog.utility.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.config.DefaultBeanNames;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import static feed.catalog.utility.Constants.*;

@Slf4j
@Service
public class FeedServiceImp implements FeedService{
    private static final ModelMapper modelMapper = new ModelMapper();

    private static final Gson gson = new GsonBuilder().setLenient().setDateFormat(DATE_PATTER).setPrettyPrinting().disableHtmlEscaping().create();

    @Autowired
    private FeedDao feedDAO;

    @Autowired
    DestinationDao destinationDao;

    @Autowired
    Producer producer;

    @Autowired
    Constants constants;

    @Autowired
    RestClient restClient;

    @Value("${kafka.schedule-kafka-adhoc}")
    private String TOPIC_ADHOC;

    @Value("${kafka.schedule-kafka}")
    private String TOPIC;

    /**
     *
     * @param feedDTO
     * @param clientId
     * @return
     */
    @Override
    public Feed create(FeedCreateDTO feedDTO,String clientId) {
        return this.execute(() ->{
            validateIfDestinationExists(clientId, feedDTO.getDestinationName(),null);
            String id = generateId(clientId,feedDTO.getFeedName());
            if (feedDAO.exists(id, Feed.class))
                throw new FeedCatalogInvocableException(String.format("Feed %s already exists",feedDTO.getFeedName()));
            Feed feed = getTargetObject(feedDTO,new Feed());
            feed.setId(id);
            feed.setClientId(clientId);
            scheduleFeed(feed,clientId);
            return create(feed);
        },String.format("Feed : %s cannot be created", feedDTO.getFeedName()), HttpStatus.CONFLICT);
    }

    /**
     *
     * @param feed
     * @return
     */
    @Override
    public Feed create(Feed feed) {
        return feedDAO.createFeed(feed);
    }

    /**
     *
     * @param feed
     * @param clientId
     * @return
     */
    @Override
    @Transactional
    public Feed update(Feed feed, String clientId) {
        return this.execute(() -> {
            scheduleFeed(feed, clientId);
            return feedDAO.updateFeed(feed);
        },String.format("FeedName : %s could not be updated", feed.getFeedName()), HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param feedUpdateDTO
     * @param name
     * @param clientId
     * @return
     */
    public Feed update(FeedUpdateDTO feedUpdateDTO, String name, String clientId) {
        Optional<Feed> optionalFeed = get(generateId(clientId,name));
        validateIfDestinationExists(clientId, feedUpdateDTO.getDestinationName(),optionalFeed.get().getDestinationName());
        if(feedUpdateDTO.isActive() && optionalFeed.get().isActive()){
            throw new FeedCatalogInvocableException("The feed is already active");
        }
        return update(getTargetObject(feedUpdateDTO,optionalFeed.get()), clientId);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Feed> get(String id) {
        return this.execute(() -> {
            Optional<Feed> optionalFeed = feedDAO.getFeed(id);
            if (optionalFeed.isPresent()) {
                return optionalFeed;
            }
            throw new FeedCatalogInvocableException("Feed does not Exists");
        },String.format("Feed : %s doesnot exists", id), HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param name
     * @param clientId
     * @return
     */
    public FeedSnapShot getDTO(String name, String clientId) {
        Optional<Feed> optionalFeed = get(generateId(clientId,name));
        return getTargetObject(optionalFeed.get(),new FeedSnapShot());
    }

    /**
     *
     * @param clientId
     * @param feedName
     */
    @Override
    @Transactional
    public void delete(String clientId, String feedName) {
        feedDAO.deleteFeed(generateId(clientId,feedName));
    }

    /**
     *
     * @param clientId
     * @return
     */
    @Override
    @Transactional
    public List<FeedCreateDTO> getAllFeedsByClient(String clientId) {
        List<FeedCreateDTO> feedCreateDTOS = new ArrayList<>();
        feedDAO.getAllFeeds(clientId).stream().parallel().map(feed -> feedCreateDTOS.add(getTargetObject(feed,new FeedCreateDTO()))).collect(Collectors.toList());
        return feedCreateDTOS;
    }

    /**
     *
     * @param clientId
     * @return
     */
    public ResponseEntity<Object> getAllEligibleFeeds(String clientId){
        String uri = restClient.contructURL(restClient.getCatalogHost(), restClient.getCatalogPort(), Constants.CATALOG_URL_DETAILS + clientId, restClient.getParameterString(restClient.getExporterTypes()));
        String response = "";
        try {
            response = restClient.getRequest(uri);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     *
     * @param feed
     * @param clientId
     */
    public void scheduleFeed(Feed feed, String clientId){
        String s = null;
        if(feed.isActive()){
            s = gson.toJson(getFeedSnapShotAdhoc(feed,clientId));
            log.info("Producer data : "+s);
            if(feed.getScheduleConfig().contains("ADHOC")){
                producer.writeMessage(TOPIC_ADHOC,s);
            }
            else
                producer.writeMessage(TOPIC,s);
            log.info("Producer data : "+s);
        }
    }

    /**
     * This service provides a snapshot for Workflow Engine to consume data and schedule Jobs
     * @param feedName
     * @param clientId
     * @return
     */
    public FeedSnapShot getFeedSnapShot(String feedName, String clientId){
        FeedSnapShot feedSnapShot = getDTO(feedName, clientId);
        Optional<Destination> optionalDestination = destinationDao.getDestination(clientId.concat(feedSnapShot.getDestinationName()));
        DestinationDTO destinationDTO;
        if(optionalDestination.isPresent()){
            destinationDTO = getTargetObject(optionalDestination.get(),new DestinationDTO());
            feedSnapShot.setDestination(destinationDTO);
            feedSnapShot.setNominalTime(LocalDateTime.now().toString());
            feedSnapShot.setClient(clientId);
            return feedSnapShot;
        }
        else
            throw new FeedCatalogInvocableException(
                    String.format(
                            "There seems to be an issue with data, " +
                                    "specially with the Destination for Feed Name : %s " +
                                    "and Clientid : %s",feedName,clientId));
    }

    /**
     *
     * @param feed
     * @param clientId
     * @return
     */
    public FeedSnapShot getFeedSnapShotAdhoc(Feed feed, String clientId){
        Optional<Destination> optionalDestination = destinationDao.getDestination(clientId.concat(feed.getDestinationName()));
        DestinationDTO destinationDTO;
        if(optionalDestination.isPresent()){
            FeedSnapShot f = getTargetObject(feed,new FeedSnapShot());
            destinationDTO = getTargetObject(optionalDestination.get(),new DestinationDTO());
            f.setDestination(destinationDTO);
            f.setNominalTime(LocalDateTime.now().toString());
            f.setClient(clientId);
            f.setWfId("Workflow-"+ UUID.randomUUID().toString());
            return f;
        }
        else
            throw new FeedCatalogInvocableException(
                    String.format(
                            "There seems to be an issue with data, " +
                                    "specially with the Destination for Feed Name : %s " +
                                    "and Clientid : %s",feed.getFeedName(),clientId));
    }

    /**
     *
     * @param clientId
     * @param destinationName
     * @return
     */
    public List<FeedCreateDTO> getAllByDestinationName(String clientId, String destinationName) {
        return this.execute(() ->{
            List<FeedCreateDTO> feedCreateDTOS = new ArrayList<>();
            feedDAO.getAllFeedsByDestinationName(clientId,destinationName).stream().parallel().map(feed -> feedCreateDTOS.add(getTargetObject(feed,new FeedCreateDTO()))).collect(Collectors.toList());
            return feedCreateDTOS;
        },String.format("No feeds found for : %s destination", destinationName), HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @return
     */
    @Override
    @Transactional
    public List<Feed> getAll() {
       throw new FeedCatalogInvocableException("Unimplemented method");
    }

    /**
     *
     * @param clientId
     * @param modelName
     * @return
     */
    public String generateId(String clientId,String modelName){
        return schemaToMD5(clientId.concat(modelName)).toString();
    }

    /**
     *
     * @param s
     * @return
     * @throws RuntimeException
     */
    public static String schemaToMD5(String s) throws RuntimeException {
        return DigestUtils.md5DigestAsHex(s.getBytes());
    }

    /**
     *
     * @param src
     * @param model
     * @param <T>
     * @return
     */
    <T> T getTargetObject(Object src,T model){
        return DTOModelMapperImpl.mapper(src,model);
    }

    /**
     *
     * @param src
     * @param model
     * @param <T>
     * @param <S>
     * @return
     */
    <T,S> T getDTOToDTO(S src,T model){
        return DTOModelMapperImpl.mapperDTO(src,model);
    }

    private void validateIfDestinationExists(String clientId, String destinationName, @Nullable String enetityDestinationName){
        String destinationNameNonNil = Optional.ofNullable(destinationName) //
                .filter(StringUtils::hasText).orElse(enetityDestinationName);
        if(!destinationDao.exists(clientId.concat(destinationNameNonNil), Destination.class))
            throw new FeedCatalogInvocableException(String.format("Destination %s does not exists",destinationName));
    }

    /**
     *
     * @param supplier
     * @param msg
     * @param STATUS
     * @param <T>
     * @return
     */
    public <T> T execute(Supplier<T> supplier, String msg,HttpStatus STATUS) {
        try {
            return supplier.get();
        } catch (FeedCatalogInvocableException exception) {
            HandlerResponse handlerResponse = HandlerResponse.builder().message(exception.getLocalizedMessage())
                    .details(Arrays.asList(msg)).errorCode(STATUS.toString()).httpStatus(STATUS).build();
            throw new FeedCatalogInvocableException(handlerResponse);
        }
        catch (Exception exception) {
            HandlerResponse handlerResponse = HandlerResponse.builder().message(exception.getLocalizedMessage())
                    .details(Arrays.asList("We seem to have a problem. We are sorry for the inconvinience. Please contact administrator")).errorCode(STATUS.toString()).httpStatus(STATUS).build();
            throw new FeedCatalogInvocableException(handlerResponse);
        }
    }
}
