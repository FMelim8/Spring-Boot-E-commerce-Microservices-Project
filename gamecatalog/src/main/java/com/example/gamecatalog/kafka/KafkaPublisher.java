package com.example.gamecatalog.kafka;

import com.example.sharedlib.dto.GameEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
public class KafkaPublisher {
    private final KafkaTemplate<String, GameEvent> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void publish(String topic, GameEvent gameEvent) {
        try {
            kafkaTemplate.send(topic, gameEvent);
        } catch (Exception e) {
            logger.error("Kafka Error",
                    kv("Service", "Gamecatalog"),
                    kv("Message", e.getMessage())
            );
        }
    }
}
