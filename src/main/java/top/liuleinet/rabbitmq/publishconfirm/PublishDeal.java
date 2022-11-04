package top.liuleinet.rabbitmq.publishconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import top.liuleinet.rabbitmq.MqUtil;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @classname: PublishDeal
 * @author: lei.liu
 * @description: 处理异步方式未被确认的消息
 * @date: 2022/11/4
 * @version: v1.0
 **/
public class PublishDeal {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = MqUtil.getChannel();
        channel.confirmSelect();
        channel.queueDeclare(MqUtil.QUEUE_NAME, false, false, false, null);
        // 准备一个队列存储消息
        ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();
        // 删除掉已经确认的消息
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            // 如果是批量的就一起清理
            if (multiple) {
                // 获取到确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = map.headMap(deliveryTag);
                confirmed.clear();
            } else {
                map.remove(deliveryTag);
            }
            System.out.println("确认的消息为：" + deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String nackMsg = map.get(deliveryTag);
            System.out.println("这个未确认消息的内容为：" + nackMsg);
        };
        channel.addConfirmListener(ackCallback, nackCallback);
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String msg = String.valueOf(i);
            long num = channel.getNextPublishSeqNo();
            channel.basicPublish("", MqUtil.QUEUE_NAME, null, msg.getBytes());
            // 记录发送的所有消息
            map.put(num, msg);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("发布了1000条批量确认消息耗时为：" + (endTime - beginTime) + "ms");
    }
}
