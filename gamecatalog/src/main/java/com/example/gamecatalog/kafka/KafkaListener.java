package com.example.gamecatalog.kafka;

import com.example.gamecatalog.service.GameService;
import com.example.sharedlib.dto.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class KafkaListener {
    private final GameService gameService;

    public KafkaListener(GameService gameService) {
        this.gameService = gameService;
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "orderEventSupplier-out-0", groupId = "order-event-group")
    public void listen(OrderEvent orderEvent) {
        gameService.gameStockCheck(orderEvent);
    }

    @Bean
    public RecordMessageConverter converter() {
        return new StringJsonMessageConverter();
    }
}
