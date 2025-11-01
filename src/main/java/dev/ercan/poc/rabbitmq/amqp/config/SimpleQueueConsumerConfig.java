package dev.ercan.poc.rabbitmq.amqp.config;

import dev.ercan.poc.rabbitmq.amqp.consumer.SimpleQueueConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SimpleQueueConsumerConfig {

  private final SimpleQueueConfig simpleQueueConfig;

  @Bean
  SimpleMessageListenerContainer simpleQueueContainer(ConnectionFactory connectionFactory,
      MessageListener simpleQueueConsumer) {
    SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
    listenerContainer.setConnectionFactory(connectionFactory);
    listenerContainer.setQueueNames(simpleQueueConfig.getQueue());
    listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
    listenerContainer.setMessageListener(simpleQueueConsumer);
    listenerContainer.setChannelTransacted(true);

    listenerContainer.setConcurrentConsumers(2);
    listenerContainer.setMaxConcurrentConsumers(4);

    return listenerContainer;
  }

  @Bean
  SimpleQueueConsumer simpleQueueConsumer() {
    return new SimpleQueueConsumer();
  }

}
