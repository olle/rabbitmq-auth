package com.studiomediatech.security.spring.rabbitmq.message;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;


public final class Envelope implements Serializable {

    private static final long serialVersionUID = -6019483538453925014L;

    private UserDetails userDetails = null;
    private Authentication authentication = null;
    private AuthenticationException authenticationException = null;

    public UserDetails getUserDetails() {

        return userDetails;
    }


    public void setUserDetails(UserDetails userDetails) {

        this.userDetails = userDetails;
    }


    public Authentication getAuthentication() {

        return authentication;
    }


    public void setAuthentication(Authentication authentication) {

        this.authentication = authentication;
    }


    public AuthenticationException getAuthenticationException() {

        return authenticationException;
    }


    public void setAuthenticationException(AuthenticationException authenticationException) {

        this.authenticationException = authenticationException;
    }


    @Override
    public String toString() {

        return "Envelope [userDetails=" + userDetails + ", authentication=" + authentication
            + ", authenticationException=" + authenticationException + "]";
    }
}
