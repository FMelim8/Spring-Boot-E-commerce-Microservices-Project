package com.example.sharedlib.exception;

import com.example.sharedlib.annotations.LoggableException;
import com.rollbar.notifier.Rollbar;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@LoggableException
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String serviceName;
    private final Rollbar rollbar;

    public CustomAuthenticationEntryPoint(@Value("${service.name}") String serviceName, Rollbar rollbar) {
        this.serviceName = serviceName;
        this.rollbar = rollbar;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        rollbar.warning(authException, "Unauthorized access attempt");

        logger.warn("Unauthorized access attempt",
                kv("Service", serviceName),
                kv("Path", request.getRequestURI()),
                kv("Response",new ServiceCustomException(
                        "Unauthorized",
                        "Full authentication is required to access this resource",
                        HttpStatus.UNAUTHORIZED.value(),
                        request.getRequestURI()
                )),
                kv("HttpStatus", "401"),
                kv("Success", false)
        );

        response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
    }
}
