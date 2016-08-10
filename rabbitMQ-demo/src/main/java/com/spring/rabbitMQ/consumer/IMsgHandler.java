package com.spring.rabbitMQ.consumer;

/**
 * 消息处理
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/5.
 */
public interface IMsgHandler {

    /**
     * 消息
     *
     * @param exchange   交换区
     * @param routingKey 路由Key(队列名称）
     * @param message    消息接收
     */
    void onMessage(String exchange, String routingKey, String message);
}
