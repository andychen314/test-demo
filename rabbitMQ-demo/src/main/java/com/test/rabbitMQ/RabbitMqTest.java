package com.test.rabbitMQ;

import com.test.rabbitMQ.core.config.RabbitMQServerConfig;
import com.test.rabbitMQ.core.DefaultEventController;
import com.test.rabbitMQ.core.send.IMsgSend;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class RabbitMqTest {

    private String defaultHost = "127.0.0.1";
    private final static int DEFAULT_PORT = 5672;
    private final static String DEFAULT_USERNAME = "chenshifeng";
    private final static String DEFAULT_PASSWORD = "123456";


    private String defaultExchange = "EXCHANGE_DIRECT_TEST";

    private String defaultQueue = "QUEUE_TEST";

    private DefaultEventController controller;

    private IMsgSend eventTemplate;

    @Before
    public void init() throws IOException {
        RabbitMQServerConfig config = new RabbitMQServerConfig(defaultHost, DEFAULT_PORT, DEFAULT_USERNAME, DEFAULT_PASSWORD);
        controller = DefaultEventController.getInstance(config);
        eventTemplate = controller.getEopEventTemplate();
        controller.add(defaultQueue, defaultExchange, new ApiProcessEventProcessor());
        controller.start();
    }

    @Test
    public void sendString() throws RabbitMQException {
        eventTemplate.send(defaultQueue, defaultExchange, "hello world");
    }

    @Test
    public void sendObject() throws RabbitMQException {
        eventTemplate.send(defaultQueue, defaultExchange, mockObj());
    }

    @Test
    public void sendTemp() throws RabbitMQException, InterruptedException {
        String tempExchange = "EXCHANGE_DIRECT_TEST_TEMP";//以前未声明的exchange
        String tempQueue = "QUEUE_TEST_TEMP";//以前未声明的queue
        eventTemplate.send(tempQueue, tempExchange, mockObj());
        //发送成功后此时不会接受到消息，还需要绑定对应的消费程序
        controller.add(tempQueue, tempExchange, new ApiProcessEventProcessor());
    }

    @After
    public void end() throws InterruptedException {
        Thread.sleep(2000);
    }

    private People mockObj() {
        People jack = new People();
        jack.setId(1);
        jack.setName("JACK");
        jack.setMale(true);

        List<People> friends = new ArrayList<>();
        friends.add(jack);
        People hanMeiMei = new People();
        hanMeiMei.setId(1);
        hanMeiMei.setName("Lucy");
        hanMeiMei.setMale(false);
        hanMeiMei.setFriends(friends);

        People liLei = new People();
        liLei.setId(2);
        liLei.setName("Lily");
        liLei.setMale(true);
        liLei.setFriends(friends);
        liLei.setSpouse(hanMeiMei);
        hanMeiMei.setSpouse(liLei);
        return hanMeiMei;
    }

    class ApiProcessEventProcessor implements EventProcesser<People> {
        @Override
        public void process(People e) {//消费程序这里只是打印信息
            System.out.println(e.getSpouse());
            System.out.println(e.getFriends());
        }
    }
}
