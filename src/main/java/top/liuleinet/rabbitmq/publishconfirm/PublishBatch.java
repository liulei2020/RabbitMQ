package top.liuleinet.rabbitmq.publishconfirm;

import com.rabbitmq.client.Channel;
import top.liuleinet.rabbitmq.MqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @classname: PublishBatch
 * @author: lei.liu
 * @description: 批量确认发布
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class PublishBatch {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = MqUtil.getChannel();
        // 开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(MqUtil.QUEUE_NAME, false, false, false, null);
        // 批量确认消息数
        int batchSize = 100;
        // 未确认消息个数
        int total = 0;
        long beginTime = System.currentTimeMillis();
        // 发布消息测试
        for (int i = 0; i < 1000; i++) {
            String msg = String.valueOf(i);
            channel.basicPublish("", MqUtil.QUEUE_NAME, null, msg.getBytes());
            total++;
            // 如果发布的消息数等于设置的批量确认消息数，就进行发布确认
            if (total == batchSize) {
                channel.waitForConfirms();
                total = 0;
            }
        }
        //为了确保还有剩余没有确认消息 再次确认
        if (total > 0) {
            channel.waitForConfirms();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("发布了1000条批量确认消息耗时为：" + (endTime - beginTime) + "ms");
        System.out.println("未确认的消息数为：" + total);
    }
}
