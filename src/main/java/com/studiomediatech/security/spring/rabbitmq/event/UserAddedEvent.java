package com.studiomediatech.security.spring.rabbitmq.event;

import org.springframework.security.core.userdetails.UserDetails;


public final class UserAddedEvent {

    private final UserDetails userDetails;

    public UserAddedEvent(UserDetails userDetails) {

        this.userDetails = userDetails;
    }

    public UserDetails getUserDetails() {

        return userDetails;
    }
}
