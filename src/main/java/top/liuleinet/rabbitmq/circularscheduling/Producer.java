package top.liuleinet.rabbitmq.circularscheduling;

import com.rabbitmq.client.Channel;
import top.liuleinet.rabbitmq.MqUtil;

import java.io.IOException;
import java.util.Scanner;
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
        Channel channel = MqUtil.getChannel();

        // 信道指定队列
        channel.queueDeclare(MqUtil.QUEUE_NAME, false, false, false, null);
        // 从控制台中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            channel.basicPublish("", MqUtil.QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成，本次的消息为----" + message);
        }
    }
}
