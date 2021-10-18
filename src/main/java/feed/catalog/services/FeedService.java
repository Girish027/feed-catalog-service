package feed.catalog.services;

import feed.catalog.api.response.dto.FeedCreateDTO;
import feed.catalog.domain.Feed;

import java.util.List;
import java.util.Optional;

/**
 * #Author ADITYA.BARIYAR
 */

public interface FeedService extends XService<Feed,String>{

    public Feed create(FeedCreateDTO feedDTO, String clientId);

    public Feed create(Feed feed);

    public Optional<Feed> get(String id);

    public Feed update(Feed Feed, String clientId);

    public void delete(String feedName, String clientId);

    public List<FeedCreateDTO> getAllFeedsByClient(String clientId);

    public List<Feed> getAll();

    //public boolean exists(String filter , Class<Feed> claz);
}
