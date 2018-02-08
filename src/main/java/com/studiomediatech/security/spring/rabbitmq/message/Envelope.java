package com.studiomediatech.security.spring.rabbitmq.message;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;


public final class Envelope implements Serializable {

    private static final long serialVersionUID = -6019483538453925014L;

    private UserDetails userDetails = null;
    private AuthenticationSuccessEvent authSuccess = null;
    private AbstractAuthenticationFailureEvent authFailure = null;

    protected Envelope() {

        // OK
    }


    private Envelope(UserDetails userDetails) {

        this.userDetails = userDetails;
    }


    private Envelope(AuthenticationSuccessEvent authSuccess) {

        this.authSuccess = authSuccess;
    }


    private Envelope(AbstractAuthenticationFailureEvent authFailure) {

        this.authFailure = authFailure;
    }

    public UserDetails getUserDetails() {

        return userDetails;
    }


    public void setUserDetails(UserDetails userDetails) {

        this.userDetails = userDetails;
    }


    public AuthenticationSuccessEvent getAuthSuccess() {

        return authSuccess;
    }


    public void setAuthSuccess(AuthenticationSuccessEvent authSuccess) {

        this.authSuccess = authSuccess;
    }


    public AbstractAuthenticationFailureEvent getAuthFailure() {

        return authFailure;
    }


    public void setAuthFailure(AbstractAuthenticationFailureEvent authFailure) {

        this.authFailure = authFailure;
    }


    public static long getSerialversionuid() {

        return serialVersionUID;
    }


    @Override
    public String toString() {

        return "Envelope [userDetails=" + userDetails + ", authSuccess=" + authSuccess + ", authFailure=" + authFailure
            + "]";
    }


    public static Envelope valueOf(UserDetails userDetails) {

        return new Envelope(userDetails);
    }


    public static Envelope valueOf(AuthenticationSuccessEvent event) {

        return new Envelope(event);
    }


    public static Envelope valueOf(AbstractAuthenticationFailureEvent event) {

        return new Envelope(event);
    }
}
