package com.example.sharedlib.aspect;

import com.example.sharedlib.annotations.Sensitive;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String serviceName;

    public LoggingAspect(@Value("${service.name}") String serviceName) {
        this.serviceName = serviceName;
    }

    @Around("@annotation(com.example.sharedlib.annotations.LoggableController) || @within(com.example.sharedlib.annotations.LoggableController)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = signature.getParameterNames();

        long start = System.currentTimeMillis();

        Map<String, Object> requests = new java.util.HashMap<>(Map.of());

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name = parameterNames != null ? parameterNames[i] : "arg" + i;
            Object value = args[i];

            if (parameter.isAnnotationPresent(RequestBody.class)) {
                value = maskSensitiveFields(value);
                requests.put(name, value);

            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVar = parameter.getAnnotation(PathVariable.class);
                String varName = !pathVar.value().isEmpty() ? pathVar.value() : name;

                requests.put(varName, value);

            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam reqParam = parameter.getAnnotation(RequestParam.class);
                String paramName = !reqParam.value().isEmpty() ? reqParam.value() : name;

                requests.put(paramName, value);

            } else {
                requests.put(name, value);
            }
        }

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        if (result instanceof ResponseEntity<?> responseEntity) {
            HttpStatusCode statusCode = responseEntity.getStatusCode();
            Object responseBody = responseEntity.getBody();

            responseBody = maskSensitiveFields(responseBody);

            logger.info("Controller Call",
                    kv("Service", serviceName),
                    kv("Operation", joinPoint.getSignature().getName()),
                    kv("Request", requests),
                    kv("Response", responseBody),
                    kv("Response-time", (end - start) + " ms"),
                    kv("HttpStatus", String.valueOf(statusCode.value())),
                    kv("Success", statusCode.is2xxSuccessful())
            );
        }

        return result;
    }

@Around("@annotation(com.example.sharedlib.annotations.LoggableException) || @within(com.example.sharedlib.annotations.LoggableException)")
    public Object logException(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof ResponseEntity<?> responseEntity) {
            HttpStatusCode statusCode = responseEntity.getStatusCode();

            logger.info("Exception",
                    kv("Service", serviceName),
                    kv("Operation", joinPoint.getSignature().getName()),
                    kv("Exception", responseEntity.getBody()),
                    kv("HttpStatus", String.valueOf(statusCode.value()))
            );
        }
        return result;
    }

    private Object maskSensitiveFields(Object original) {
        if (original == null) {
            return null;
        }

        try {
            Object copy = original.getClass().getDeclaredConstructor().newInstance();

            BeanUtils.copyProperties(original, copy);

            Field[] fields = copy.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Sensitive.class)) {
                    field.setAccessible(true);
                    if (field.getType().equals(String.class)) {
                        field.set(copy, "******");
                    }
                }
            }

            return copy;

        } catch (Exception e) {
            return original;
        }
    }
}
