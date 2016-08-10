package com.test.rabbitMQ;

import com.spring.rabbitMQ.consumer.DefaultMsgHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 客户端
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/5.
 */
public class TestConsumer {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("rabbit-consumer-test.xml");
        DefaultMsgHandler bean = context.getBean(DefaultMsgHandler.class);
        System.out.println(bean);
        System.in.read();
    }
}
