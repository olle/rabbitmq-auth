package com.studiomediatech.security.spring.rabbitmq.event;

import org.springframework.security.authentication.event.AuthenticationSuccessEvent;


public class AuthSuccessEvent {

    private final AuthenticationSuccessEvent authSuccess;

    public AuthSuccessEvent(AuthenticationSuccessEvent authSuccess) {

        this.authSuccess = authSuccess;
    }

    public AuthenticationSuccessEvent getAuthSuccess() {

        return authSuccess;
    }
}
