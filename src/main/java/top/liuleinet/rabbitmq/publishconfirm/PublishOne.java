package top.liuleinet.rabbitmq.publishconfirm;

import com.rabbitmq.client.Channel;
import top.liuleinet.rabbitmq.MqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @classname: PublishOne
 * @author: lei.liu
 * @description: 单个确认发布
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class PublishOne {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = MqUtil.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(MqUtil.QUEUE_NAME, false, false, false, null);
        int total = 0;
        long beginTime = System.currentTimeMillis();
        // 发布消息测试
        for (int i = 0; i < 100; i++) {
            String msg = String.valueOf(i);
            channel.basicPublish("", MqUtil.QUEUE_NAME, null, msg.getBytes());
            // 发布确认，返回true则表示成功发布，返回false可以重新发布，也可以使用超时时间。
            boolean flag = channel.waitForConfirms();
            if (flag) {
                total++;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("发布了100条单独确认消息耗时为：" + (endTime - beginTime) + "ms");
        System.out.println("成功发布了消息数为：" + total);
    }
}
