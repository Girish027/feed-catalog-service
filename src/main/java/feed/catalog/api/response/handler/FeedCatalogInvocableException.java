package feed.catalog.api.response.handler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class FeedCatalogInvocableException extends RuntimeException{

    private HandlerResponse response ;

    public FeedCatalogInvocableException(HandlerResponse msg) {
        this.response = msg;
    }

    public FeedCatalogInvocableException(String msg) {
        super(msg);
    }

    public FeedCatalogInvocableException() {
        super();
    }

    public FeedCatalogInvocableException(String msg , Throwable clause) {
        super(msg,clause);
    }
}
