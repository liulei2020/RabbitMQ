package top.liuleinet.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @classname: Consumer
 * @author: lei.liu
 * @description: 消费者
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constant.HOSTNAME);
        factory.setUsername(Constant.USERNAME);
        factory.setPassword(Constant.PASSWORD);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 消费者接收的回调
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("接收到的消息为："+new String(message.getBody()));
        };

        // 消费者被取消消费的回调
        CancelCallback cancelCallback = consumerTag -> System.out.println("消费者取消了消费"+ "\n"+ consumerTag);
        /*
         * 消费者消费消息
         * 1. 队列的名称
         * 2. 消费成功之后是否要自动应答。true表示自动应答，false表示要手动应答
         * 3. 消费者接收的回调
         * 4. 消费者被取消消费的回调
         */
        channel.basicConsume(Constant.QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
