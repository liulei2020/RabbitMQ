package top.liuleinet.rabbitmq.messagereply;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.liuleinet.rabbitmq.MqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @classname: Consumer1
 * @author: lei.liu
 * @description: 消费者1
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class Consumer1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtil.getChannel();
        System.out.println("消费者**1**等待接收消息， 该消费者处理消息时间较短");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者**1**开始消费消息");
            String deliverMessage = new String(message.getBody());
            // 模拟消费消息需要一秒钟
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("消费者**1**接收到了消息，消息为： " + deliverMessage);
            // 手动应答(不批量)
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者**1**取消了消费消息，当前消息信息为：");
            System.out.println(consumerTag);
        };

        // 第二个参数标识为false，表示不使用自动应答
        channel.basicConsume(MqUtil.QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
