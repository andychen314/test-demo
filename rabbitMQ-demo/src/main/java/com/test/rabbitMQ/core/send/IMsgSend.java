package com.test.rabbitMQ.core.send;

import com.test.rabbitMQ.core.codec.ICodecFactory;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public interface IMsgSend {
    /**
     * 发送消息
     *
     * @param exchangeName 交换区名称
     * @param queueName    队列名称
     * @param content      内容
     */
    void send(String exchangeName, String queueName, Object content);

    /**
     * 发送消息
     *
     * @param exchangeName 交换区名称
     * @param queueName    队列名称
     * @param content      内容
     * @param codecFactory 转码工方法
     */
    void send(String exchangeName, String queueName, Object content, ICodecFactory codecFactory);
}
