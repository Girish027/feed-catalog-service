package feed.catalog.dao;

import com.datastax.oss.driver.api.mapper.annotations.Update;
import com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy;
import feed.catalog.domain.Feed;
import feed.catalog.domain.AuditFeed;

import java.util.List;
import java.util.Optional;

/**
 * DAO interface for Feed to perform CRUD operation.
 */
public interface FeedDao {
    public Feed createFeed(Feed feed);

    public AuditFeed createFeedPipeline(AuditFeed auditFeed);

    public Optional<Feed> getFeed(String feedName);

    @Update(nullSavingStrategy = NullSavingStrategy.DO_NOT_SET)
    public Feed updateFeed(Feed feed);

    public void deleteFeed(String feedName);

    public List<Feed> getAllFeeds(String clientId);

    public List<Feed> getAllFeedsByDestinationName(String clientId, String destinationName);

    public boolean exists(String filter ,Class<?> claz);
}
