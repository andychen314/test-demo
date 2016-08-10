package com.test.rabbitMQ.core.send;

import com.test.rabbitMQ.EventMessage;
import com.test.rabbitMQ.RabbitMQException;
import com.test.rabbitMQ.core.codec.DefaultCodecFactory;
import com.test.rabbitMQ.core.codec.ICodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.StringUtils;

/**
 * 消息发送
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class DefaultMsgSend implements IMsgSend {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMsgSend.class);

    private AmqpTemplate eventAmqpTemplate;

    private ICodecFactory defaultCodecFactory;

    public DefaultMsgSend(AmqpTemplate eopAmqpTemplate, ICodecFactory defaultCodecFactory) {
        this.eventAmqpTemplate = eopAmqpTemplate;
        this.defaultCodecFactory = defaultCodecFactory;
    }

    public DefaultMsgSend(AmqpTemplate eopAmqpTemplate) {
        this.eventAmqpTemplate = eopAmqpTemplate;
        this.defaultCodecFactory = new DefaultCodecFactory();
    }

    @Override
    public void send(String exchangeName, String queueName, Object content) {
        this.send(queueName, exchangeName, content, defaultCodecFactory);
    }

    @Override
    public void send(String exchangeName, String queueName, Object content,
                     ICodecFactory codecFactory) {
        if (StringUtils.isEmpty(queueName) || StringUtils.isEmpty(exchangeName)) {
            throw new RabbitMQException("queueName exchangeName can not be empty.");
        }

        byte[] eventContentBytes = null;
        if (codecFactory == null) {
            if (content == null) {
                logger.warn("Find eventContent is null,are you sure...");
            } else {
                throw new RabbitMQException("codecFactory must not be null ,unless eventContent is null");
            }
        } else {
            eventContentBytes = codecFactory.serialize(content);
        }
        // 构造成Message
        EventMessage msg = new EventMessage(queueName, exchangeName, eventContentBytes);
        try {
            eventAmqpTemplate.convertAndSend(exchangeName, queueName, msg);
        } catch (AmqpException e) {
            logger.error("send event fail. Event Message : [" + content + "]", e);
            throw new RabbitMQException("send event fail", e);
        }
    }
}
