package com.example.gameorder.kafka;

import com.example.sharedlib.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
public class KafkaPublisher {

    @Autowired
    KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void publish(String topic, OrderEvent orderEvent) {
        try {
            kafkaTemplate.send(topic, orderEvent);
        } catch (Exception e) {
            logger.error("Kafka Error",
                    kv("Service", "Gameorder"),
                    kv("Message", e.getMessage())
            );
        }
    }
}
