package com.studiomediatech.security.spring.rabbitmq.event;

import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;
import com.studiomediatech.security.spring.rabbitmq.message.Envelope;

import org.springframework.amqp.core.AmqpTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.event.EventListener;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import org.springframework.stereotype.Component;


@Component
public class AuthenticationFailedHandler implements Loggable {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AuthenticationFailedHandler(AmqpTemplate amqpTemplate) {

        this.amqpTemplate = amqpTemplate;
    }

    @EventListener
    public void on(AbstractAuthenticationFailureEvent event) {

        logger().info("Sending auth failed");
        amqpTemplate.convertAndSend("authentication", "auth.failed", Envelope.valueOf(event));
    }
}
