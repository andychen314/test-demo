package com.test.rabbitMQ.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class MessageErrorHandler implements ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        logger.error("RabbitMQ happen a error:" + t.getMessage(), t);
    }
}
