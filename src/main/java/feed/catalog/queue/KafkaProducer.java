package feed.catalog.queue;

import feed.catalog.api.response.handler.FeedCatalogInvocableException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class KafkaProducer implements Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void writeMessage(String topic, String message) {

        try {
            kafkaTemplate.send(topic, message).get();
        } catch (InterruptedException ex) {
            throw new FeedCatalogInvocableException
                    (String.format("Interrupted with some unknown error : %s",ex.getLocalizedMessage()));
        } catch (ExecutionException ex) {
            throw new FeedCatalogInvocableException
                    (String.format("Execution interrupted with some unknown error : %s",ex.getCause().getLocalizedMessage()));
        }
    }

    @Override
    public void close(){
        kafkaTemplate.flush();
    }

}

