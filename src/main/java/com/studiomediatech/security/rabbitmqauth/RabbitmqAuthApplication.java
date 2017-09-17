package com.studiomediatech.security.rabbitmqauth;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.springframework.amqp.core.AbstractDeclarable;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class RabbitmqAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqAuthApplication.class, args);
	}

	@Bean
	public AuthenticationProvider our(final ApplicationEventPublisher publisher) {

		return new AuthenticationProvider() {

			@Override
			public boolean supports(Class<?> authentication) {
				return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
			}

			@Override
			public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

				if (Objects.isNull(authentication)) {
					return authentication;
				}

				String credentials = (String) authentication.getCredentials();

				if (Objects.isNull(credentials)) {
					return authentication;
				}

				boolean isAuthenticated = credentials.equalsIgnoreCase("foobar");
				if (!isAuthenticated) {
					return authentication;
				}

				publisher.publishEvent(new IsAuthenticated());
				return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
						authentication.getCredentials(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
			}
		};
	}

	@Configuration
	@EnableWebSecurity
	public static class SecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.antMatchers("/user").hasAnyRole("USER")
				.antMatchers("/admin").hasAnyRole("ADMIN")
				.antMatchers("/**").fullyAuthenticated()
			.and()
				.formLogin().permitAll();
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

	// @Component
	// @EnableAsync
	// @EnableAspectJAutoProxy(proxyTargetClass = true)
	// public static class Publisher implements Loggable {
	//
	// @Autowired
	// AmqpTemplate amqpTemplate;
	//
	// @EventListener
	// public void on(Publisher.Authenticated _event) {
	// send();
	// }
	//
	// @Async
	// private void send() {
	// amqpTemplate.send("authentication", "test",
	// MessageBuilder.withBody("helo".getBytes()).build());
	// }
	//
	//
	//
	//
	// static class Authenticated {
	// // OK
	// }
	// }
}
