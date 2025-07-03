package com.example.sharedlib.exception;

import com.example.sharedlib.annotations.LoggableException;
import com.rollbar.notifier.Rollbar;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@LoggableException
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String serviceName;
    private final Rollbar rollbar;

    public CustomAccessDeniedHandler(@Value("${service.name}") String serviceName, Rollbar rollbar) {
        this.serviceName = serviceName;
        this.rollbar = rollbar;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        logger.warn("Forbidden access attempt",
                kv("Service", serviceName),
                kv("Path", request.getRequestURI()),
                kv("Response",new ServiceCustomException(
                        "Forbidden",
                        "Access Denied",
                        HttpStatus.FORBIDDEN.value(),
                        request.getRequestURI()
                )),
                kv("HttpStatus", "403"),
                kv("Success", false)
        );

        rollbar.warning(accessDeniedException, "Forbidden access attempt");

        response.sendError(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage());
    }
}
