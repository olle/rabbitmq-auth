package com.studiomediatech.security.spring.rabbitmq.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.studiomediatech.security.spring.rabbitmq.event.AuthHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthHandler authHandler;

    @Autowired
    public SecurityConfig(AuthHandler authHandler) {

        this.authHandler = authHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/user")
            .hasAnyRole("USER")
            .antMatchers("/admin")
            .hasAnyRole("ADMIN")
            .antMatchers("/**")
            .fullyAuthenticated()
            .and()
            .formLogin()
            .successHandler(authHandler)
            .failureHandler(authHandler)
            .permitAll();
    }
}
