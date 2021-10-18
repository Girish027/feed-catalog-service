package feed.catalog.api.response.handler;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
public class HandlerResponse {
    private String message;
    private List<String> details;
    private String errorCode;
    private HttpStatus httpStatus;
}
