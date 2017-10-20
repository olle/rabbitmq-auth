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
import org.springframework.context.annotation.ComponentScan;
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

	@Configuration
	@EnableWebSecurity
	public static class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthHandler authHandler;

    @Autowired
    public SecurityConfig(AuthHandler authHandler) {
        this.authHandler = authHandler;
    }

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.antMatchers("/user").hasAnyRole("USER")
				.antMatchers("/admin").hasAnyRole("ADMIN")
				.antMatchers("/**").fullyAuthenticated()
			.and()
				.formLogin()
        .successHandler(authHandler)
        .failureHandler(authHandler)
        .permitAll();
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

@Controller
class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

@Component
class AuthHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, Loggable {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AuthHandler(AmqpTemplate amqpTemplate) {

        this.amqpTemplate = amqpTemplate;
        logger().info("Created new auth handler {}", this);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger().info("Sending auth success");
        amqpTemplate.send(
                "authentication",
                "auth.success",
                MessageBuilder.withBody("{}".getBytes()).setContentEncoding("UTF-8").build());
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger().info("Sending auth failed");
        amqpTemplate.send(
                "authentication",
                "auth.failed",
                MessageBuilder.withBody("{}".getBytes()).setContentEncoding("UTF-8").build());
    }
}

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
