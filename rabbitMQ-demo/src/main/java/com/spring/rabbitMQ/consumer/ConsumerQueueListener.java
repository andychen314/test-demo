package com.spring.rabbitMQ.consumer;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/5.
 */
public class ConsumerQueueListener implements MessageListener {

    private IMsgHandler msgHandler;

    public ConsumerQueueListener(IMsgHandler msgHandler) {
        this.msgHandler = msgHandler;
    }

    @Override
    public void onMessage(Message message) {
        String exchange = message.getMessageProperties().getReceivedExchange();
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        byte[] body = message.getBody();
        msgHandler.onMessage(exchange, routingKey, new String(body));
    }
}
