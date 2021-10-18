package feed.catalog.dao;

import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.api.response.handler.HandlerResponse;
import feed.catalog.api.response.utils.Template;
import feed.catalog.api.response.utils.validator.annotations.implementations.DTOModelMapperImpl;
import feed.catalog.domain.Feed;
import feed.catalog.domain.AuditFeed;
import feed.catalog.repositories.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static feed.catalog.utility.Constants.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class FeedDaoImp implements FeedDao{

        @Autowired
        private Template template;

        @Autowired
        private FeedRepository repository;

        public <T> T execute(Supplier<T> supplier, String operation, T entity, String exceptionMessage) {
            try {
                AuditFeed auditFeed = new AuditFeed();
                auditFeed.setOperation(operation);
                auditFeed.setFeedId(UUID.randomUUID().toString());
                auditFeed.setFeedId(UUID.randomUUID().toString());
                auditFeed.setUser("API");
                switch (operation) {
                    case "DELETE":auditFeed(deriveAuditObject(entity.toString(), auditFeed), operation);
                        break;
                     default:auditFeed(deriveAuditObject(entity, auditFeed), operation);
                }
                return supplier.get();
            } catch (FeedCatalogInvocableException exception) {
                throw new FeedCatalogInvocableException(
                        HandlerResponse.builder()
                                .message(exceptionMessage)
                                .details(Arrays.asList(String.format("Operation : %s could not be completed and %s", operation, exceptionMessage)))
                                .build());
            }
            catch (Exception exception) {
                throw new FeedCatalogInvocableException(
                        HandlerResponse.builder()
                                .message(exceptionMessage)
                                .details(Arrays.asList(exception.getLocalizedMessage()))
                                .build());
            }
        }

        @Override
        public List<Feed> getAllFeeds(String clientId) {
            return repository.findAllByClientId(clientId);
        }

        @Override
        public boolean exists(String filter ,Class<?> claz) {
            return template.exists(filter,claz);
        }

        @Override
        public List<Feed> getAllFeedsByDestinationName(String clientId, String destinationName) {
            return repository.findAllByClientIdAndDestinationName(clientId,destinationName);
        }
        @Override
        public Feed createFeed(Feed feed) {
            return this.execute(() -> {
                return template.create(feed);
            }, "CREATE", feed, CREATE_FEED_EXCEPTION);
        }

        @Override
        public AuditFeed createFeedPipeline(AuditFeed feed) {
            return this.execute(() -> {
                return template.create(feed);
            }, "CREATE", feed, CREATE_FEED_EXCEPTION);
        }

        @Override
        public Optional<Feed> getFeed(String feedName) {
            return template.findById(feedName, Feed.class);
        }

        @Override
        public Feed updateFeed(Feed feed) {
            return this.execute(() -> {
                return template.update(feed);
            }, "UPDATE", feed, UPDATE_FEED_EXCEPTION);

        }

        @Override
        public void deleteFeed(String id) {
            this.execute(() -> {
                template.deleteById(id, Feed.class);
                return 0;
            }, "DELETE",id, DELETE_FEED_EXCEPTION);
        }

        private <T> T deriveAuditObject(String id, T trg){
            return DTOModelMapperImpl.mapper
                    (getFeed(id).get(),trg);
        }

    private <T,S> T deriveAuditObject(S feedSrc,T trg){
        return DTOModelMapperImpl.mapper
                (feedSrc,trg);
    }

        <S> void auditFeed(S feedPipeline,String operation){
            template.create(feedPipeline);
        }
}
