package top.liuleinet.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @classname: MqUtil
 * @author: lei.liu13@hand-china.com
 * @description: 获取信道的工具类
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class MqUtil {

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "Hello";
    /**
     * 主机名
     */
    public static final String HOSTNAME = "127.0.0.1";
    /**
     * 用户名
     */
    public static final String USERNAME = "guest";
    /**
     * 密码
     */
    public static final String PASSWORD = "guest";
    /**
     * 获取信道
     * @return  Channel
     */
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
