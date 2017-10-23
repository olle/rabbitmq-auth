package com.studiomediatech.security.spring.rabbitmq.event;

import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;
import com.studiomediatech.security.spring.rabbitmq.message.Envelope;

import org.springframework.amqp.core.AmqpTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.event.EventListener;

import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import org.springframework.stereotype.Component;


@Component
public class SuccessfullyAuthenticatedHandler implements Loggable {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public SuccessfullyAuthenticatedHandler(AmqpTemplate amqpTemplate) {

        this.amqpTemplate = amqpTemplate;
        logger().info("Created new auth handler {}", this);
    }

    @EventListener
    public void on(AuthenticationSuccessEvent event) {

        logger().info("Sending auth success {}", event);

        Envelope envelope = new Envelope();
        envelope.setAuthSuccess(event);

        amqpTemplate.convertAndSend("authentication", "auth.success", envelope);
    }
}
