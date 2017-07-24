package com.studiomediatech.security.rabbitmqauth;

import org.springframework.amqp.core.AbstractDeclarable;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableRabbit
public class RabbitmqAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqAuthApplication.class, args);
	}
	
	@Configuration
	public static class RabbitMqConfig implements Loggable {
				
		@Bean
		public FanoutExchange authExchange() {
			return (FanoutExchange) logAndReturn(new FanoutExchange("authentication"));
		}

		private AbstractDeclarable logAndReturn(AbstractDeclarable declared) {
			logger().info("Created {}", declared);
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
}
