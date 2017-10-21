package com.studiomediatech.security.spring.rabbitmq.message;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;

@Component
public class Consumer implements Loggable {

    public Consumer() {

        logger().info("(*)---- Starting consumer...");
    }

    @RabbitListener(queues = "#{@authQueue}")
    public void onAuthMessage(Message message) {

        logger().info(">>---> Received auth event: {}", message);
    }
}