package com.example.sharedlib.exception;

import com.example.sharedlib.annotations.LoggableException;
import com.rollbar.notifier.Rollbar;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

@ControllerAdvice()
@LoggableException
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Rollbar rollbar;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(ServiceCustomException.class)
    public ResponseEntity<?> handleCustomException(ServiceCustomException exception,
                                                   HttpServletRequest request) {
        return new ResponseEntity<>(
                new ServiceCustomException(
                        exception.getError(),
                        exception.getMessage(),
                        exception.getStatus(),
                        request.getRequestURI()
                ),
                HttpStatusCode.valueOf(exception.getStatus())
        );
    }

    @ExceptionHandler({
            CannotCreateTransactionException.class
    })
    public ResponseEntity<?> handleTransactionException(Exception exception, HttpServletRequest request) {

        rollbar.error(exception, "Transaction Error");

        return new ResponseEntity<>(
                new ServiceCustomException(
                        "Internal Server Error",
                        "Database Connection Error",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getRequestURI()
                ),
                HttpStatusCode.valueOf(500)
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ServiceCustomException> handleIllegalStateException(IllegalStateException exception,
                                                                               HttpServletRequest request) {
        return new ResponseEntity<>(
                new ServiceCustomException(
                        "Bad Request",
                        exception.getMessage(),
                        400,
                        request.getRequestURI()
                ),
                HttpStatusCode.valueOf(400)
        );
    }

    @ExceptionHandler({
            SocketTimeoutException.class
    })
    public ResponseEntity<?> handleTimeoutException(Exception exception,
                                                    HttpServletRequest request) {

        rollbar.warning(exception, "Connection Timeout");

        return new ResponseEntity<>(
                new ServiceCustomException(
                        "Internal Server Error",
                        "Service Connection Timed Out",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getRequestURI()
                ),
                HttpStatusCode.valueOf(500)
        );
    }


    @ExceptionHandler({
            FeignException.ServiceUnavailable.class,
            ConnectException.class,
            HttpHostConnectException.class
    })
    public ResponseEntity<ServiceCustomException> handleFeignServiceException(FeignException.ServiceUnavailable exception,
                                                                              HttpServletRequest request) {

        rollbar.error(exception, "Service Unavailable");

        logger.error(
                "Connection Error",
                kv("message", exception.getMessage())
        );

        return new ResponseEntity<>(
                new ServiceCustomException(
                        "Service Unavailable",
                        "Service temporarily unavailable",
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        request.getRequestURI()
                ),
                HttpStatusCode.valueOf(503)
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request){

        rollbar.info(exception, "Bad Request");

        ServiceCustomException customException = new ServiceCustomException(
                "Bad Request",
                "Invalid request format",
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        return new ResponseEntity<>(
                new ServiceCustomException(
                        "Bad Request",
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getDescription(false).replace("uri=", "")
                ),
                HttpStatusCode.valueOf(400)
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        return new ResponseEntity<>(
                new ServiceCustomException(
                        "Bad Request",
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getDescription(false).replace("uri=", "")
                ),
                HttpStatusCode.valueOf(400)
        );
    }
}
