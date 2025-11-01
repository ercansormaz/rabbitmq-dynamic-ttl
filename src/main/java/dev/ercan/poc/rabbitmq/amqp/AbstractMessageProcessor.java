package dev.ercan.poc.rabbitmq.amqp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

public abstract class AbstractMessageProcessor<T> {

  private final Jackson2JsonMessageConverter jsonMessageConverter;

  @SuppressWarnings("unchecked")
  public AbstractMessageProcessor() {
    // get class type from type arguments
    Type superClass = getClass().getGenericSuperclass();
    Type tType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    Class<T> classType = (Class<T>) tType;

    // prepare class mapper
    DefaultClassMapper classMapper = new DefaultClassMapper();
    classMapper.setDefaultType(classType);
    classMapper.setTrustedPackages("*");

    // prepare JSON message converter
    jsonMessageConverter = new Jackson2JsonMessageConverter();
    jsonMessageConverter.setClassMapper(classMapper);
  }


  @SuppressWarnings("unchecked")
  public T convertToInternalMessage(Message message) {
    // specify content type of the message to parse
    message.getMessageProperties().setContentType("application/json");
    // parse message
    return (T) jsonMessageConverter.fromMessage(message);
  }

  public Message convertToAmqpMessage(T message) {
    // prepare message properties
    MessageProperties messageProperties = new MessageProperties();
    return jsonMessageConverter.toMessage(message, messageProperties);
  }

}
