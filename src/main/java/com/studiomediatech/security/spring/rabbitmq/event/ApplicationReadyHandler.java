package com.studiomediatech.security.spring.rabbitmq.event;

import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;
import com.studiomediatech.security.spring.rabbitmq.message.Envelope;

import org.springframework.amqp.core.AmqpTemplate;

import org.springframework.boot.context.event.ApplicationReadyEvent;

import org.springframework.context.event.EventListener;

import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Component;


@Component
class ApplicationReadyHandler implements Loggable {

    private static final String EXCHANGE = "authentication";

    private static final String USER_ADDED = "user.added";

    private final AmqpTemplate amqpTemplate;

    public ApplicationReadyHandler(AmqpTemplate amqpTemplate) {

        this.amqpTemplate = amqpTemplate;
    }

    @EventListener
    public void on(ApplicationReadyEvent _event) {

        logger().info("Handling application ready event {}", _event);

        amqpTemplate.convertAndSend(EXCHANGE, USER_ADDED,
            Envelope.valueOf(User.withUsername("admin").password("admin").roles("USER", "ADMIN", "ACTUATOR").build()));

        amqpTemplate.convertAndSend(EXCHANGE, USER_ADDED,
            Envelope.valueOf(User.withUsername("user").password("user").roles("USER").build()));

        amqpTemplate.convertAndSend(EXCHANGE, USER_ADDED,
            Envelope.valueOf(User.withUsername("other").password("other").roles("OTHER").build()));
    }
}
