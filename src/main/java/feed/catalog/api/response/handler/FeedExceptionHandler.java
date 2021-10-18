package feed.catalog.api.response.handler;


import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class FeedExceptionHandler extends ResponseEntityExceptionHandler {

    private HandlerResponse buildResponse(List<String> details){
        HandlerResponse reponse = HandlerResponse.builder().details(details).errorCode(HttpStatus.BAD_REQUEST.toString()).message("Failed!!!").httpStatus(HttpStatus.BAD_REQUEST).build();
        return reponse;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = ex.getBindingResult().getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.toList());
        HandlerResponse reponse = HandlerResponse.builder().details(details).errorCode(HttpStatus.BAD_REQUEST.toString()).message("Failed!!!").httpStatus(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = Arrays.asList(ex.getLocalizedMessage());
        HandlerResponse reponse = HandlerResponse.builder().details(details).errorCode(HttpStatus.BAD_REQUEST.toString()).message("Failed!!!").httpStatus(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(FeedCatalogInvocableException.class)
    public final ResponseEntity<Object> handleFeedCatalogInvocableException(Exception ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return generateErrorResponse((FeedCatalogInvocableException) ex, httpStatus);
    }

    public static void handleExceptionResolver(BindingResult result, Class<?>... _class) throws NoSuchMethodException,MethodArgumentNotValidException{
        if (result.hasErrors()) {
            String callerMethod = new Throwable().getStackTrace()[1].getMethodName();
            Class invoker = _class[0];
            Class<?>[] _parameters = Arrays.copyOfRange(_class,1,_class.length);
            MethodParameter parameter = new MethodParameter(invoker.getMethod(callerMethod, _parameters), 0);
            throw new MethodArgumentNotValidException(parameter, result);
        }
    }

    private ResponseEntity<Object> generateErrorResponse(FeedCatalogInvocableException exception, HttpStatus httpStatus) {
        final HandlerResponse error = Optional.ofNullable(exception.getResponse())
                .orElse(HandlerResponse.builder().details(Arrays.asList(exception.getLocalizedMessage())).build());
        httpStatus = null != exception.getResponse() ?
                null != exception.getResponse().getHttpStatus() ? exception.getResponse().getHttpStatus() : httpStatus : httpStatus;
        //log.info(String.format("Response code:%s, Response body:%s", httpStatus, error.toString()));
        return new ResponseEntity<>(error, httpStatus);
    }
}
