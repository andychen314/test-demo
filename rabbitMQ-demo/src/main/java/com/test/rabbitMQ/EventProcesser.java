package com.test.rabbitMQ;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public interface EventProcesser<T> {

    public void process(T t);
}
