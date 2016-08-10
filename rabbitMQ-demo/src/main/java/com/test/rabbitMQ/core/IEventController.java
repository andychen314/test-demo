package com.test.rabbitMQ.core;

import com.test.rabbitMQ.EventProcesser;
import com.test.rabbitMQ.core.send.IMsgSend;

import java.util.Map;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public interface IEventController {

    /**
     * 控制器启动方法
     */
    void start();

    /**
     * 获取发送模版
     */
    IMsgSend getEopEventTemplate();
    /**
     * 绑定消费程序到对应的exchange和queue
     */
    IEventController add(String queueName, String exchangeName, EventProcesser eventProcesser);

    /*in map, the key is queue name, but value is exchange name*/
    IEventController add(Map<String,String> bindings, EventProcesser eventProcesser);

}
