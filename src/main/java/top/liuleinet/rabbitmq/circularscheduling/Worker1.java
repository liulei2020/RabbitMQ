package top.liuleinet.rabbitmq.circularscheduling;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.liuleinet.rabbitmq.MqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @classname: Worker1
 * @author: lei.liu
 * @description: 第1个工作线程
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class Worker1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println("接收到的消息：" +"\n" + new String(message.getBody()));

        CancelCallback cancelCallback = consumerTag -> System.out.println("消费者取消消费这个消息" + "\n" +consumerTag);

        System.out.println("当前工作线程为**1**，正在等待接收消息--------");
        // 接收消息
        channel.basicConsume(MqUtil.QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
