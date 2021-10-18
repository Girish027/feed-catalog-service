package feed.catalog.controllers;

import feed.catalog.api.response.dto.FeedCreateDTO;
import feed.catalog.api.response.dto.FeedUpdateDTO;
import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.domain.Feed;
import feed.catalog.api.response.dto.FeedSnapShot;
import feed.catalog.services.FeedServiceImp;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(path = "/{clientId}/")
public class FeedController {

    public static final String APPLICATION_JSON = "application/json";

    FeedServiceImp feedService;

    @Autowired
    public void setFeedService(FeedServiceImp feedService) {
        this.feedService = feedService;
    }

    @PostMapping(path = "feeds", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<String> createFeed(@RequestBody @Valid FeedCreateDTO feed, @PathVariable String clientId) throws FeedCatalogInvocableException {
        Feed createdFeed = feedService.create(feed,clientId);
        return new Resource<String>(createdFeed.getFeedName(), ControllerLinkBuilder.linkTo(methodOn(this.getClass()).getFeed(createdFeed.getFeedName(),clientId)).withSelfRel());
    }

    @PutMapping(path = "feeds/{feedName}" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Resource<String> editFeed(@Valid @RequestBody FeedUpdateDTO feedUpdateDTO,@PathVariable("feedName") String feedName ,@PathVariable("clientId") String clientId ) throws MethodArgumentNotValidException,NoSuchMethodException,IllegalStateException{
        Feed editedFeed = feedService.update(feedUpdateDTO,feedName,clientId);
        return new Resource<>(editedFeed.getFeedName(), ControllerLinkBuilder.linkTo(methodOn(this.getClass()).getFeed(editedFeed.getFeedName(),clientId)).withSelfRel());
    }

    @Transactional
    @GetMapping(path = "feeds/{feedName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getFeed(@PathVariable("feedName") String feedId,@PathVariable("clientId") String clientId ){
        FeedCreateDTO optionalFeed = feedService.getDTO(feedId,clientId);
        return ResponseEntity.status(HttpStatus.OK).body(optionalFeed);
    }

    @GetMapping(path = "feeds")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllFeeds(@PathVariable("clientId") String clientId ) {
        List<FeedCreateDTO> feeds = feedService.getAllFeedsByClient(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(feeds); //TODO href.
    }

    @GetMapping(path = "feeds/destinations")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllFeedsByDestinationName(@PathVariable("clientId") String clientId, @RequestParam("destinationName") String destinationName) {
        List<FeedCreateDTO> feeds = feedService.getAllByDestinationName(clientId,destinationName);
        return ResponseEntity.status(HttpStatus.OK).body(feeds); //TODO href.
    }

    @DeleteMapping(path = "feeds/{name}/delete")
    @ResponseStatus(HttpStatus.OK)
    public Resource deleteFeed(@PathVariable("clientId") String clientId, @PathVariable("name") String feedName ) throws FeedCatalogInvocableException {
        feedService.delete(clientId, feedName);
        return new Resource("Deleted " + feedName + "!!!");
    }

    @GetMapping(path = "schedulableFeeds")
    @Timed
    public ResponseEntity<Object> getAllFeedToBeScheduled(@PathVariable("clientId") String clientId) {
        return feedService.getAllEligibleFeeds(clientId);
    }

    @GetMapping(path = "feeds/{feedName}/cadence-feed-snapshot")
    @Timed
    public ResponseEntity<Object> cadenceFeedSnapshot(@PathVariable("feedName") String feedName, @PathVariable("clientId") String clientId) {
        FeedSnapShot feedSnapShot = feedService.getFeedSnapShot(feedName,clientId);
        return ResponseEntity.status(HttpStatus.OK).body(feedSnapShot);
    }
}
