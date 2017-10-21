package com.studiomediatech.security.spring.rabbitmq.autoconfigure;

import com.studiomediatech.security.spring.rabbitmq.event.AuthenticationEventHandler;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEventHandler authHandler;

    @Autowired
    public SecurityConfig(AuthenticationEventHandler authHandler) {

        this.authHandler = authHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        //J-
        http.authorizeRequests()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/**").authenticated()
            .and()
                .formLogin().permitAll()
                .successHandler(authHandler)
                .failureHandler(authHandler)
                .defaultSuccessUrl("/user")
            .and()
                .logout()
                .permitAll()
                .invalidateHttpSession(true);
        //J+
    }
}
