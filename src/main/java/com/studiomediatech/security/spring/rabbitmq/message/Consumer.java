package com.studiomediatech.security.spring.rabbitmq.message;

import com.studiomediatech.security.spring.rabbitmq.event.AuthFailedEvent;
import com.studiomediatech.security.spring.rabbitmq.event.AuthSuccessEvent;
import com.studiomediatech.security.spring.rabbitmq.event.UserAddedEvent;
import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.context.ApplicationEventPublisher;

import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import org.springframework.stereotype.Component;


@Component
public class Consumer implements Loggable {

    private ApplicationEventPublisher publisher;

    public Consumer(ApplicationEventPublisher publisher) {

        this.publisher = publisher;
    }

    @RabbitListener(queues = "#{@authQueue}")
    public void onAuthMessage(@Payload Envelope envelope, Message message) {

        logger().info(">>---> Received message {} with envelope {}", message, envelope);

        try {
            String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();

            if ("user.added".equalsIgnoreCase(receivedRoutingKey)) {
                publisher.publishEvent(new UserAddedEvent(envelope.getUserDetails()));
            } else if ("auth.success".equalsIgnoreCase(receivedRoutingKey)) {
                publisher.publishEvent(new AuthSuccessEvent(envelope.getAuthSuccess()));
            } else if ("auth.failed".equalsIgnoreCase(receivedRoutingKey)) {
                AbstractAuthenticationFailureEvent authFailure = envelope.getAuthFailure();
                publisher.publishEvent(new AuthFailedEvent(authFailure));
            }
        } catch (Throwable e) {
            // OK
        }
    }
}
