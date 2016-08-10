package com.test.rabbitMQ.core;

import com.test.rabbitMQ.RabbitMQException;
import com.test.rabbitMQ.core.codec.DefaultCodecFactory;
import com.test.rabbitMQ.core.codec.ICodecFactory;
import com.test.rabbitMQ.core.config.RabbitMQServerConfig;
import com.test.rabbitMQ.core.send.IMsgSend;
import com.test.rabbitMQ.handler.MessageAdapterHandler;
import com.test.rabbitMQ.handler.MessageErrorHandler;
import com.test.rabbitMQ.core.send.DefaultMsgSend;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 和rabbitmq通信的控制器，主要负责：
 * <p>1、和rabbitmq建立连接</p>
 * <p>2、声明exChange和queue以及它们的绑定关系</p>
 * <p>3、启动消息监听容器，并将不同消息的处理者绑定到对应的exchange和queue上</p>
 * <p>4、持有消息发送模版以及所有exchange、queue和绑定关系的本地缓存</p>
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class RabbitMQConnection {

    private CachingConnectionFactory rabbitConnectionFactory;

    private RabbitMQServerConfig config;

    private RabbitAdmin rabbitAdmin;

    private ICodecFactory defaultCodecFactory = new DefaultCodecFactory();

    private SimpleMessageListenerContainer msgListenerContainer; // rabbitMQ msg listener container

    private MessageAdapterHandler msgAdapterHandler = new MessageAdapterHandler();

    private MessageConverter serializerMessageConverter = new SerializerMessageConverter(); // 直接指定
    //queue cache, key is exchangeName
    private Map<String, DirectExchange> exchanges = new HashMap<>();
    //queue cache, key is queueName
    private Map<String, Queue> queues = new HashMap<>();
    //bind relation of queue to exchange cache, value is exchangeName | queueName
    private Set<String> binded = new HashSet<>();

    //发送方法
    private IMsgSend msgSend;

    private AtomicBoolean isStarted = new AtomicBoolean(false);


    public RabbitMQConnection(RabbitMQServerConfig config) {
        if (config == null) {
            throw new RabbitMQException("Config can not be null.");
        }
        this.config = config;
        initRabbitConnectionFactory();
        // 初始化AmqpAdmin
        rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory);
        // 初始化RabbitTemplate
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        rabbitTemplate.setMessageConverter(serializerMessageConverter);
        msgSend = new DefaultMsgSend(rabbitTemplate);
        //todo
    }

    /**
     * 初始化rabbitmq连接
     */
    private void initRabbitConnectionFactory() {
        rabbitConnectionFactory = new CachingConnectionFactory();
        rabbitConnectionFactory.setHost(config.getServerHost());
        rabbitConnectionFactory.setChannelCacheSize(config.getEventMsgProcessNum());
        rabbitConnectionFactory.setPort(config.getPort());
        rabbitConnectionFactory.setUsername(config.getUserName());
        rabbitConnectionFactory.setPassword(config.getPassword());
        if (!StringUtils.isEmpty(config.getVirtualHost())) {
            rabbitConnectionFactory.setVirtualHost(config.getVirtualHost());
        }
    }

    /**
     * 注销程序
     */
    public synchronized void destroy() throws Exception {
        if (!isStarted.get()) {
            return;
        }
        msgListenerContainer.stop();
        msgSend = null;
        rabbitAdmin = null;
        rabbitConnectionFactory.destroy();
    }

    /**
     * 初始化消息监听器容器
     */
    private void initMsgListenerAdapter() {
        MessageListener listener = new MessageListenerAdapter(msgAdapterHandler, serializerMessageConverter);
        msgListenerContainer = new SimpleMessageListenerContainer();
        msgListenerContainer.setConnectionFactory(rabbitConnectionFactory);
        msgListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        msgListenerContainer.setMessageListener(listener);
        msgListenerContainer.setErrorHandler(new MessageErrorHandler());
        msgListenerContainer.setPrefetchCount(config.getPrefetchSize()); // 设置每个消费者消息的预取值
        msgListenerContainer.setConcurrentConsumers(config.getEventMsgProcessNum());
        msgListenerContainer.setTxSize(config.getPrefetchSize());//设置有事务时处理的消息数
        msgListenerContainer.setQueues(queues.values().toArray(new Queue[queues.size()]));
        msgListenerContainer.start();
    }

    /**
     * exchange和queue是否已经绑定
     */
    protected boolean beBinded(String exchangeName, String queueName) {
        return binded.contains(exchangeName + "|" + queueName);
    }

    /**
     * 声明exchange和queue已经它们的绑定关系
     */
    protected synchronized void declareBinding(String exchangeName, String queueName) {
        String bindRelation = exchangeName + "|" + queueName;
        if (binded.contains(bindRelation)) return;

        boolean needBinding = false;
        DirectExchange directExchange = exchanges.get(exchangeName);
        if (directExchange == null) {
            directExchange = new DirectExchange(exchangeName, true, false, null);
            exchanges.put(exchangeName, directExchange);
            rabbitAdmin.declareExchange(directExchange);//声明exchange
            needBinding = true;
        }

        Queue queue = queues.get(queueName);
        if (queue == null) {
            queue = new Queue(queueName, true, false, false);
            queues.put(queueName, queue);
            rabbitAdmin.declareQueue(queue);    //声明queue
            needBinding = true;
        }

        if (needBinding) {
            Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);//将queue绑定到exchange
            rabbitAdmin.declareBinding(binding);//声明绑定关系
            binded.add(bindRelation);
        }
    }
}
