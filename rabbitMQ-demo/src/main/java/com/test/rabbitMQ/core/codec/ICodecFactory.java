package com.test.rabbitMQ.core.codec;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public interface ICodecFactory {
    byte[] serialize(Object obj);

    Object deSerialize(byte[] in);
}
