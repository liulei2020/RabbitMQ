package top.liuleinet.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @classname: Producer
 * @author: lei.liu
 * @description: 生产者
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接的配置，ip，用户名和密码
        factory.setHost(MqUtil.HOSTNAME);
        factory.setUsername(MqUtil.USERNAME);
        factory.setPassword(MqUtil.PASSWORD);
        // 创建连接
        Connection connection = factory.newConnection();
        // 通过连接来创建一个信道
        Channel channel = connection.createChannel();
        /*
         * 通过一个信道来生成一个队列
         * 1. 队列的名称
         * 2. true表示声明的是一个持久化队列，该队列会在服务器重启后依旧存活
         * 3. true表示声明的是一个独占队列，被限制在这个连接中。
         * 4. 最后一个消费者断开连接之后，该队列是否自动删除。true表示自动删除，false表示不自动删除
         * 5. 其他参数
         */
        channel.queueDeclare(MqUtil.QUEUE_NAME,false,false,false,null);

        String message = "Hello";

        /*
         * 发送一个消息
         * 1. 发送到哪个交换机
         * 2. 路由的key值是哪个，本次是队列的名称
         * 3. 其他参数信息
         * 4. 发送消息的消息体
         */
        channel.basicPublish("", MqUtil.QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送成功");
    }
}
