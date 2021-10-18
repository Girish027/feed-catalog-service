package feed.catalog.queue;

import org.springframework.stereotype.Service;

@Service
public interface Producer {
    void writeMessage(String topic, String message);
    void close();
}
