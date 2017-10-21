package com.studiomediatech.security.spring.rabbitmq.event;

import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;
import com.studiomediatech.security.spring.rabbitmq.message.Envelope;

import org.springframework.amqp.core.AmqpTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.event.ApplicationReadyEvent;

import org.springframework.context.event.EventListener;

import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Component;


@Component
public class ApplicationReadyHandler implements Loggable {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public ApplicationReadyHandler(AmqpTemplate amqpTemplate) {

        this.amqpTemplate = amqpTemplate;
    }

    @EventListener
    public void on(ApplicationReadyEvent _event) {

        logger().info("Handling application ready event {}", _event);

        Envelope envelope = new Envelope();
        envelope.setUserDetails(User.withUsername("foo").password("bar").roles("USER", "ADMIN", "ACTUATOR").build());

        amqpTemplate.convertAndSend("authentication", "user.added", envelope);
    }
}
