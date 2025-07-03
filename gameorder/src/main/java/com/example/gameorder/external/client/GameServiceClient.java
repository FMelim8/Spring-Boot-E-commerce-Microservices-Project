package com.example.gameorder.external.client;

import com.example.gameorder.config.FeignClientConfig;
import com.example.gameorder.payloads.response.OrderResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "${gamecatalog.service.name}", contextId = "GameServiceRequest", path = "${gamecatalog.service.basePath:}/api/games", configuration = FeignClientConfig.class)
public interface GameServiceClient {

    @GetMapping("/{id}")
   @CircuitBreaker(name = "DEFAULT_CIRCUITBREAKER", fallbackMethod = "getGameByIdFallback")
   @Bulkhead(name = "DEFAULT_BULKHEAD", fallbackMethod = "getGameByIdBulkhead")
    ResponseEntity<OrderResponse.GameDetails> getGameById(
            @PathVariable("id") long gameId
    );


   default ResponseEntity<OrderResponse.GameDetails> getGameByIdFallback(long gameId, Throwable t){
       return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
   }

   default ResponseEntity<OrderResponse.GameDetails> getGameByIdBulkhead(long gameId, Throwable t){
       return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
   }
    @GetMapping("/batch")
   @CircuitBreaker(name = "DEFAULT_CIRCUITBREAKER", fallbackMethod = "getGamesByIdsFallback")
   @Bulkhead(name = "DEFAULT_BULKHEAD", fallbackMethod = "getGamesByIdsBulkhead")
    ResponseEntity<List<OrderResponse.GameDetails>> getGamesByIds(@RequestParam List<Long> gameIds);

   default ResponseEntity<List<OrderResponse.GameDetails>> getGamesByIdsFallback(List<Long> gameIds, Throwable t){
       return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
   }

   default ResponseEntity<List<OrderResponse.GameDetails>> getGamesByIdsBulkhead(List<Long> gameIds, Throwable t){
       return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
   }
}
