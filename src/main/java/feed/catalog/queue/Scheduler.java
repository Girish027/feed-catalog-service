package feed.catalog.queue;

import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import feed.catalog.config.KafkaConfig;
import feed.catalog.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class Scheduler {
    @Autowired
    private KafkaConfig kafkaConfig;
    @Autowired
    private Producer producer;


    @Async
    public Future<String> scheduleFeed(String feed) {
        try {
            publishToScheduleTopic(kafkaConfig.getScheduleKafka(), feed);
        } catch (JSONException e) {
            throw new FeedCatalogInvocableException("Error while scheduling feed : " + feed, e);
        }
        return new AsyncResult<String>("scheduled feed");
    }

    public void publishToScheduleTopic(String topic, String feed) throws JSONException {
        producer.writeMessage(topic, getFeedId(feed));
    }

    private String getFeedId(String feed) throws JSONException {
        JSONObject jsonObject = new JSONObject(feed);
        return jsonObject.getString(Constants.FEED_ID);
    }
  
    public void close(){
        producer.close();
    }

}

