package com.spring.rabbitMQ.consumer;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/5.
 */
public class DefaultMsgHandler implements IMsgHandler {
    @Override
    public void onMessage(String exchange, String routingKey, String message) {
        //todo 消息处理
        System.out.println(message);
    }
}
