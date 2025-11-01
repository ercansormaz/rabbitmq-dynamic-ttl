package dev.ercan.poc.rabbitmq.amqp.producer;

import dev.ercan.poc.rabbitmq.amqp.AbstractMessageProcessor;
import dev.ercan.poc.rabbitmq.amqp.config.DelayedQueueConfig;
import dev.ercan.poc.rabbitmq.amqp.model.QueueMessage;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DelayedQueueProducer extends AbstractMessageProcessor<QueueMessage> {

  private final RabbitTemplate rabbitTemplate;
  private final DelayedQueueConfig delayedQueueConfig;

  public void publish(String message, Integer delayMs) {
    QueueMessage queueMessage = new QueueMessage(new Date(), message, delayMs);

    Message amqpMessage = convertToAmqpMessage(queueMessage);
    amqpMessage.getMessageProperties().setExpiration(String.valueOf(delayMs));

    rabbitTemplate.send(delayedQueueConfig.getExchange(), null, amqpMessage);
  }

}
