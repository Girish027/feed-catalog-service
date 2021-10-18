package feed.catalog.queue;

import feed.catalog.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KafkaProducerTest {
    @Mock private KafkaTemplate kafkaTemplate;
    @Spy private KafkaProducer prodSpyObj = new KafkaProducer(kafkaTemplate);
    @InjectMocks private KafkaProducer kafkaProducer;

    @Test
    public void TestWriteMessage_ValidFeed_ShouldPass() {
        doNothing().when(prodSpyObj).writeMessage(Constants.TOPIC, Constants.DUMMY_MSG);
        prodSpyObj.writeMessage(Constants.TOPIC, Constants.DUMMY_MSG);
    }

}
