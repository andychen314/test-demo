package com.test.rabbitMQ.core.config;

/**
 * RabbitMQ 服务器连接配置
 *
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/8/3.
 */
public class RabbitMQServerConfig {
    /**
     * 事件消息处理线程数，默认是 CPU核数 * 2
     */
    private final static int DEFAULT_PROCESS_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;

    private static final int PREFETCH_SIZE = 1;

    private String serverHost;

    private int port;

    private String userName;

    private String password;

    private String virtualHost;

    /**
     * 和rabbitmq建立连接的超时时间
     */
    private int connectionTimeout = 0;

    /**
     * 事件消息处理线程数，默认是 CPU核数 * 2
     */
    private int eventMsgProcessNum;

    /**
     * 每次消费消息的预取值
     */
    private int prefetchSize;

    /**
     * 初始化服务连接配置
     *
     * @param serverHost 服务地址
     * @param port       端口
     * @param userName   用户名
     * @param password   密码
     */
    public RabbitMQServerConfig(String serverHost, int port, String userName, String password) {
        this(serverHost, port, userName, password, null, 0, DEFAULT_PROCESS_THREAD_NUM, PREFETCH_SIZE);
    }

    /**
     * 初始化服务连接配置
     *
     * @param serverHost         服务地址
     * @param port               端口
     * @param userName           用户名
     * @param password           密码
     * @param virtualHost        虚拟主机
     * @param connectionTimeout  连接超时时间
     * @param eventMsgProcessNum 线程处理数（默认 CPU核心数*2）
     * @param prefetchSize       每次消费消息的预取值
     */
    public RabbitMQServerConfig(String serverHost, int port, String userName,
                                String password, String virtualHost, int connectionTimeout,
                                int eventMsgProcessNum, int prefetchSize) {
        this.serverHost = serverHost;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.virtualHost = virtualHost;
        this.connectionTimeout = connectionTimeout > 0 ? connectionTimeout : 0;
        this.eventMsgProcessNum = eventMsgProcessNum > 0 ? eventMsgProcessNum : DEFAULT_PROCESS_THREAD_NUM;
        this.prefetchSize = prefetchSize > 0 ? prefetchSize : PREFETCH_SIZE;
    }

    public String getServerHost() {
        return serverHost;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getEventMsgProcessNum() {
        return eventMsgProcessNum;
    }

    public int getPrefetchSize() {
        return prefetchSize;
    }
}
