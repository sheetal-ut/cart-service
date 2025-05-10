package com.ps.cartservice.kafka;

import com.ps.cartservice.model.CartEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartEventProducer {
    private final KafkaTemplate<String, CartEvent> kafkaTemplate;
    private final String topicName;

    public CartEventProducer(KafkaTemplate<String, CartEvent> kafkaTemplate,
                             @Value("${cart.kafka.topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendCartEvent(CartEvent cartEvent) {
        kafkaTemplate.send(topicName, cartEvent);
    }

}
