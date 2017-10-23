package com.studiomediatech.security.spring.rabbitmq.event;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;


public class AuthFailedEvent {

    private final AbstractAuthenticationFailureEvent authFailure;

    public AuthFailedEvent(AbstractAuthenticationFailureEvent authFailure) {

        this.authFailure = authFailure;
    }

    public AbstractAuthenticationFailureEvent getAuthFailure() {

        return authFailure;
    }
}
