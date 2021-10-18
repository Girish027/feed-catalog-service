package feed.catalog.queue;

import feed.catalog.config.KafkaConfig;
import feed.catalog.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerTest {
    @Mock private Producer producer;
    @Mock private KafkaConfig kafkaConfig;
    @Spy private Scheduler schedulerSpyObj;
    @InjectMocks private Scheduler scheduler;

    @Test
    public void TestScheduleFeed_ValidFeed_ShouldPass() throws ExecutionException, InterruptedException {
        assertEquals("scheduled feed" ,scheduler.scheduleFeed(Constants.FEED).get());
    }

    @Test
    public void TestScheduleFeed_InvalidFeed_ShouldThrowException() {
        assertThrows(RuntimeException.class, () -> scheduler.scheduleFeed(Constants.TEXT));
    }

    @Test
    public void TestPublishToKafka_ValidFeed_ShouldPass() throws JSONException {
        doNothing().when(schedulerSpyObj).publishToScheduleTopic(Constants.TOPIC, Constants.FEED);
        schedulerSpyObj.publishToScheduleTopic(Constants.TOPIC, Constants.FEED);
    }

    @Test
    public void TestPublishToKafka_InvalidFeed_ShouldThrowException() throws JSONException {
        doThrow(new JSONException(Constants.TEXT)).when(schedulerSpyObj).publishToScheduleTopic(Constants.TOPIC, Constants.TEXT);
        assertThrows(JSONException.class, () -> schedulerSpyObj.publishToScheduleTopic(Constants.TOPIC, Constants.TEXT));
    }

}
