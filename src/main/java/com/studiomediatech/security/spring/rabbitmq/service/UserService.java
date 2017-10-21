package com.studiomediatech.security.spring.rabbitmq.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
class UserService implements UserDetailsService {

    private final InMemoryUserDetailsManager manager;

    public UserService() {

        this.manager = new InMemoryUserDetailsManager();
        this.manager.createUser(User.withUsername("foo").password("bar").roles("USER", "ADMIN").build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.manager.loadUserByUsername(username);
    }
}