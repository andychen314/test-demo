package com.spring.rabbitMQ.core.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/5.
 */
@Service
public class MsgSendIml implements IMsgSend {
    @Autowired
    private AmqpTemplate amqpTemplate;

    private final static Logger logger = LoggerFactory.getLogger(MsgSendIml.class);

    @Override
    public void sendData(String exchangeName, String queueKey,Object object){
        try {
            amqpTemplate.convertAndSend(exchangeName,queueKey, object);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
