package com.spring.rabbitMQ.core.send;

/**
 * 消息发送接口
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/5.
 */
public interface IMsgSend {
    /**
     * 发送消息到指定队列
     *
     * @param exchangeName 交换区名称
     * @param queueKey     队列名称
     * @param object       发送消息对象
     */
    void sendData(String exchangeName, String queueKey, Object object);
}
