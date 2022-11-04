package top.liuleinet.rabbitmq.publishconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import top.liuleinet.rabbitmq.MqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * @classname: PublishAsync
 * @author: lei.liu
 * @description: 异步确认发布
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class PublishAsync {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = MqUtil.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(MqUtil.QUEUE_NAME, false, false, false, null);
        // 监听器消息确认成功的回调
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("这个确认消息为" + deliveryTag);
        };
        // 监听器消息确认失败的回调（这里可以直接重发，或者是先记录下来）
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("这个未确认消息为" + deliveryTag);
        };
        // 准备一个监听器，监听哪些消息发布成功或者失败(异步通知)
        channel.addConfirmListener(ackCallback, nackCallback);
        long beginTime = System.currentTimeMillis();
        // 发布消息测试
        for (int i = 0; i < 1000; i++) {
            String msg = String.valueOf(i);
            // 这里只负责发送消息，不负责确认，确认消息由监听器负责
            channel.basicPublish("", MqUtil.QUEUE_NAME, null, msg.getBytes());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("发布了1000条批量确认消息耗时为：" + (endTime - beginTime) + "ms");
    }
}
