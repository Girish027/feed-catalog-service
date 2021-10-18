package feed.catalog.repositories;

import feed.catalog.domain.Feed;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends CassandraRepository<Feed,String> {

    Optional<Feed> findByFeedName(final String feedName);

    @AllowFiltering
    List<Feed> findAllByClientIdAndDestinationName(final String clientId,final String destinationName);

    List<Feed> findAllByClientId(final String clientId);

}
