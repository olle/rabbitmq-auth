package com.studiomediatech.security.spring.rabbitmq.service;

import com.studiomediatech.security.spring.rabbitmq.UserService;
import com.studiomediatech.security.spring.rabbitmq.event.UserAddedEvent;
import com.studiomediatech.security.spring.rabbitmq.logging.Loggable;

import org.springframework.context.event.EventListener;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.stereotype.Component;


@Component
class UserServiceImpl implements UserService, Loggable {

    private final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    @Override
    public void createUser(UserDetails userDetails) {

        manager.createUser(userDetails);
    }


    @EventListener
    public void on(UserAddedEvent event) {

        UserDetails userDetails = event.getUserDetails();
        logger().info("Creating new user {}", userDetails);
        this.manager.createUser(userDetails);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.manager.loadUserByUsername(username);
    }
}
