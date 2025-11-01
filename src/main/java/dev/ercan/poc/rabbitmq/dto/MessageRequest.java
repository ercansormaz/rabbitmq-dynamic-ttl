package dev.ercan.poc.rabbitmq.dto;

public record MessageRequest(String message, Integer delayMs) {

}
