package com.test.rabbitMQ;

import com.spring.rabbitMQ.core.send.MsgSendIml;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/5.
 */
public class TestServerQueue {
    private MsgSendIml msgSendIml;

    final String queue_key = "test_queue_key";
    final String exchangeName = "test-mq-exchange";

    @Before
    public void init() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("rabbit-server-test.xml");
        msgSendIml = context.getBean(MsgSendIml.class);
    }

    @Test
    public void send() throws IOException, InterruptedException {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Andy");
        map.put("age", "30");
        long start = System.currentTimeMillis();
        System.out.println("START ==" + start);
        for (int i = 0; i < 100000; i++) {
            msgSendIml.sendData(exchangeName, queue_key, map);
        }
        System.out.println("time ==" + (System.currentTimeMillis() - start));
        Thread.sleep(10 * 1000);
    }
}
