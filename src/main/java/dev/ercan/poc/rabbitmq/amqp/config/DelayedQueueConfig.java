package dev.ercan.poc.rabbitmq.amqp.config;

import java.util.Map;
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
public class DelayedQueueConfig {

  @Value("${rabbitmq.delayed.exchange}")
  private String exchange;

  @Value("${rabbitmq.delayed.queue}")
  private String queue;

  @Value("${rabbitmq.delayed.wait.queue}")
  private String waitQueue;

  @Bean
  Queue delayedWaitQueue() {
    Map<String, Object> queueArgs = Map.of(
        "x-dead-letter-exchange", "",
        "x-dead-letter-routing-key", queue
    );

    return new Queue(waitQueue, true, false, false, queueArgs);
  }

  @Bean
  Queue delayedQueue() {
    return new Queue(queue, true, false, false);
  }

  @Bean
  FanoutExchange delayedExchange() {
    return new FanoutExchange(exchange, true, false);
  }

  @Bean
  Binding delayedBinding(FanoutExchange delayedExchange, Queue delayedWaitQueue) {
    return BindingBuilder.bind(delayedWaitQueue).to(delayedExchange);
  }

}
