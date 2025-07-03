package com.example.gameorder.kafka;

import com.example.gameorder.service.OrderService;
import com.example.sharedlib.dto.GameEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaListener {

    private final OrderService orderService;


    @org.springframework.kafka.annotation.KafkaListener(topics = "gameEventSupplier-out-0", groupId = "game-event-group")
    public void listen(GameEvent gameEvent) {
        orderService.updateOrder(gameEvent);
    }

    @Bean
    public RecordMessageConverter converter() {
        return new StringJsonMessageConverter();
    }
}
