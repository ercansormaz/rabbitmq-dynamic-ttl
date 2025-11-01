package dev.ercan.poc.rabbitmq.amqp.model;

import java.util.Date;

public record QueueMessage(Date publishDate, String message, Integer delayMs) {

}
