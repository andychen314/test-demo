package com.test.rabbitMQ.core.codec;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.test.rabbitMQ.RabbitMQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class DefaultCodecFactory implements ICodecFactory {

    private final Logger logger = LoggerFactory.getLogger(DefaultCodecFactory.class);

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream baos = null;
        HessianOutput output = null;
        try {
            baos = new ByteArrayOutputStream(1024);
            output = new HessianOutput(baos);
            output.startCall();
            output.writeObject(obj);
            output.completeCall();
        } catch (final IOException ex) {
            this.logger.error("HessionCodecFactory serialize：" + ex.getMessage());
            throw new RabbitMQException("HessionCodecFactory serialize：" + ex.getMessage());
        } finally {
            if (output != null) {
                try {
                    baos.close();
                } catch (final IOException ex) {
                    this.logger.error("Failed to close stream.", ex);
                }
            }
        }
        return baos.toByteArray();
    }

    @Override
    public Object deSerialize(byte[] in) {
        Object obj = null;
        ByteArrayInputStream bais = null;
        HessianInput input = null;
        try {
            bais = new ByteArrayInputStream(in);
            input = new HessianInput(bais);
            input.startReply();
            obj = input.readObject();
            input.completeReply();
        } catch (final IOException ex) {
            this.logger.error("HessionCodecFactory serialize：" + ex.getMessage());
            throw new RabbitMQException("HessionCodecFactory serialize：" + ex.getMessage());
        } catch (final Throwable e) {
            this.logger.error("Failed to decode object.", e);
        } finally {
            if (input != null) {
                try {
                    bais.close();
                } catch (final IOException ex) {
                    this.logger.error("Failed to close stream.", ex);
                }
            }
        }
        return obj;
    }

}
