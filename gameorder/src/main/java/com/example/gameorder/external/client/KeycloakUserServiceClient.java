package com.example.gameorder.external.client;

import com.example.gameorder.config.FeignClientConfig;
import com.example.gameorder.payloads.request.UserRequest;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${keycloak.service.name}", configuration = FeignClientConfig.class)
public interface KeycloakUserServiceClient {

    @GetMapping("${keycloak.service.basePath:}/api/userDetails/batch")
   @CircuitBreaker(name = "DEFAULT_CIRCUITBREAKER", fallbackMethod = "keycloakListFallback")
   @Bulkhead(name = "DEFAULT_BULKHEAD", fallbackMethod = "keycloakListBulkhead")
    ResponseEntity<List<UserRequest>> getUsersByIds(@RequestParam List<Long> userIds);

   default ResponseEntity<UserRequest> keycloakListFallback(Throwable t){
       return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
   }

  default ResponseEntity<UserRequest> keycloakListBulkhead(Throwable t){
      return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
  }

    @GetMapping("${keycloak.service.basePath:}/api/userDetails")
   @CircuitBreaker(name = "DEFAULT_CIRCUITBREAKER", fallbackMethod = "keycloakFallback")
   @Bulkhead(name = "DEFAULT_BULKHEAD", fallbackMethod = "keycloakBulkhead")
    ResponseEntity<UserRequest> getCurrentUserInfo();

   default ResponseEntity<UserRequest> keycloakFallback(Throwable t){
       return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
   }

   default ResponseEntity<UserRequest> keycloakBulkhead(Throwable t){
       return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
   }

    @GetMapping("${keycloak.service.basePath:}/api/userDetails/{id}")
   @CircuitBreaker(name = "DEFAULT_CIRCUITBREAKER", fallbackMethod = "keycloakByIdFallback")
   @Bulkhead(name = "DEFAULT_BULKHEAD", fallbackMethod = "keycloakByIdBulkhead")
    ResponseEntity<UserRequest> getUserInfoById(@PathVariable long id);

   default ResponseEntity<UserRequest> keycloakByIdFallback(Throwable t){
       return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
   }

  default ResponseEntity<UserRequest> keycloakByIdBulkhead(Throwable t){
      return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
  }
}
