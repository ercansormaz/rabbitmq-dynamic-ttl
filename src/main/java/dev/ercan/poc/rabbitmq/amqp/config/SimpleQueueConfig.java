package dev.ercan.poc.rabbitmq.amqp.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SimpleQueueConfig {

  @Value("${rabbitmq.simple.exchange}")
  private String exchange;

  @Value("${rabbitmq.simple.queue}")
  private String queue;

  @Bean
  Queue simpleQueue() {
    return new Queue(queue, true, false, false);
  }

  @Bean
  FanoutExchange simpleExchange() {
    return new FanoutExchange(exchange, true, false);
  }

  @Bean
  Binding simpleBinding(FanoutExchange simpleExchange, Queue simpleQueue) {
    return BindingBuilder.bind(simpleQueue).to(simpleExchange);
  }

}
