package com.studiomediatech.security.rabbitmqauth;

import java.util.Objects;
import java.util.Optional;

import org.springframework.amqp.core.AbstractDeclarable;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class RabbitmqAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqAuthApplication.class, args);
	}

	@Bean
	public AuthenticationProvider our() {

		return new AuthenticationProvider() {

			@Override
			public boolean supports(Class<?> authentication) {
				return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
			}

			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {

				if (Objects.nonNull(authentication)) {
					String credentials = (String) authentication.getCredentials();
					authentication.setAuthenticated(credentials.equalsIgnoreCase("foobar"));
				}

				return authentication;
			}
		};
	}

	@Configuration
	@EnableWebSecurity
	public static class SecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/**").fullyAuthenticated().and().formLogin().permitAll();
		}
	}

	@Configuration
	@EnableRabbit
	public static class RabbitMqConfig implements Loggable {

		@Bean
		public FanoutExchange authExchange() {
			return (FanoutExchange) logAndReturn(new FanoutExchange("authentication"));
		}

		private AbstractDeclarable logAndReturn(AbstractDeclarable declared) {
			logger().info("///// Created {}", declared);
			return declared;
		}

		@Bean
		public Queue authQueue() {
			return (Queue) logAndReturn(QueueBuilder.nonDurable().exclusive().autoDelete().build());
		}

		@Bean
		Binding authQueueBinding() {
			return (Binding) logAndReturn(BindingBuilder.bind(authQueue()).to(authExchange()));
		}
	}

	@Component
	public static class Consumer implements Loggable {

		public Consumer() {
			logger().info("(*)---- Starting consumer...");
		}

		@RabbitListener(queues = "#{@authQueue}")
		public void onAuthMessage(Message message) {
			logger().info(">>---> Received auth event: {}", message);
		}
	}
}
