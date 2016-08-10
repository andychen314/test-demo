package com.test.rabbitMQ;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class RabbitMQException extends RuntimeException {

    public RabbitMQException(String msg, Exception e) {
        super(msg, e);
    }

    public RabbitMQException(String msg) {
        super(msg);
    }

    public RabbitMQException(Exception e) {
        super(e);
    }
}
