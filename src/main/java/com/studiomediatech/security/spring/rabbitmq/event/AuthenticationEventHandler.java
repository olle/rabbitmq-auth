package com.studiomediatech.security.spring.rabbitmq.event;

import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;
import com.studiomediatech.security.spring.rabbitmq.message.Envelope;

import org.springframework.amqp.core.AmqpTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AuthenticationEventHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler,
    Loggable {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AuthenticationEventHandler(AmqpTemplate amqpTemplate) {

        this.amqpTemplate = amqpTemplate;
        logger().info("Created new auth handler {}", this);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        logger().info("Sending auth success {}", authentication);

        Envelope envelope = new Envelope();
        envelope.setAuthentication(authentication);

        amqpTemplate.convertAndSend("authentication", "auth.success", envelope);
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        logger().info("Sending auth failed");

        Envelope envelope = new Envelope();
        envelope.setAuthenticationException(exception);

        amqpTemplate.convertAndSend("authentication", "auth.failed", envelope);
    }
}
