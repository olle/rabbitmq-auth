package com.studiomediatech.security.spring.rabbitmq;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    void createUser(UserDetails userDetails);
}
