package dev.ercan.poc.rabbitmq.amqp.consumer;

import dev.ercan.poc.rabbitmq.amqp.AbstractMessageProcessor;
import dev.ercan.poc.rabbitmq.amqp.model.QueueMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

@Slf4j
public class DelayedQueueConsumer extends AbstractMessageProcessor<QueueMessage> implements
    MessageListener {

  @Override
  public void onMessage(Message message) {
    QueueMessage amqpMessage = convertToInternalMessage(message);

    log.info("[DELAYED_QUEUE_CONSUMER] [PUBLISH_DATE={}] [MESSAGE={}] [DELAY={}]",
        amqpMessage.publishDate(), amqpMessage.message(), amqpMessage.delayMs());
  }
}
