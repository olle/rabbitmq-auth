package com.studiomediatech.security.spring.rabbitmq.event;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.stereotype.Component;

import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AuthHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, Loggable {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AuthHandler(AmqpTemplate amqpTemplate) {

        this.amqpTemplate = amqpTemplate;
        logger().info("Created new auth handler {}", this);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        logger().info("Sending auth success");
        amqpTemplate.send("authentication", "auth.success",
            MessageBuilder.withBody("{}".getBytes()).setContentEncoding("UTF-8").build());
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        logger().info("Sending auth failed");
        amqpTemplate.send("authentication", "auth.failed",
            MessageBuilder.withBody("{}".getBytes()).setContentEncoding("UTF-8").build());
    }
}
