package dev.ercan.poc.rabbitmq.controller;

import dev.ercan.poc.rabbitmq.amqp.producer.DelayedQueueProducer;
import dev.ercan.poc.rabbitmq.amqp.producer.SimpleQueueProducer;
import dev.ercan.poc.rabbitmq.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/message")
public class MessageController {

  private final SimpleQueueProducer simpleQueueProducer;
  private final DelayedQueueProducer delayedQueueProducer;

  @PostMapping("/simple")
  public void publishSimpleMessage(@RequestBody MessageRequest request) {
    simpleQueueProducer.publish(request.message(), request.delayMs());
  }

  @PostMapping("/delayed")
  public void publishDelayedMessage(@RequestBody MessageRequest request) {
    delayedQueueProducer.publish(request.message(), request.delayMs());
  }

}
