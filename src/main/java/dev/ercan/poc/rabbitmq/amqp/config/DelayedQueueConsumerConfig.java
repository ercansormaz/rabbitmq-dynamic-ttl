package dev.ercan.poc.rabbitmq.amqp.config;

import dev.ercan.poc.rabbitmq.amqp.consumer.DelayedQueueConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DelayedQueueConsumerConfig {

  private final DelayedQueueConfig delayedQueueConfig;

  @Bean
  SimpleMessageListenerContainer delayedQueueContainer(ConnectionFactory connectionFactory,
      MessageListener delayedQueueConsumer) {
    SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
    listenerContainer.setConnectionFactory(connectionFactory);
    listenerContainer.setQueueNames(delayedQueueConfig.getQueue());
    listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
    listenerContainer.setMessageListener(delayedQueueConsumer);
    listenerContainer.setChannelTransacted(true);

    listenerContainer.setConcurrentConsumers(2);
    listenerContainer.setMaxConcurrentConsumers(4);

    return listenerContainer;
  }

  @Bean
  DelayedQueueConsumer delayedQueueConsumer() {
    return new DelayedQueueConsumer();
  }

}
