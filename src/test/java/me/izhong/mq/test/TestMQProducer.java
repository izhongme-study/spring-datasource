package me.izhong.mq.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class TestMQProducer {

    public static final String producerGroup = "group_test2";
    //	public static final String rocketMqAddr = "172.30.251.75:9876";
    public static final String rocketMqAddr = "10.10.51.212:9876";

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(rocketMqAddr);
        producer.setRetryTimesWhenSendFailed(10);//失败的 情况发送10次
        producer.start();

        for (int i = 0; i < 1; i++) {
            try {
                Message msg = new Message("topic_test2",// topic
                        "*",// tag
                        ("RocketMQ4").getBytes()// body
                );
                SendResult sendResult = producer.send(msg,1000);//消息在1S内没有发送成功，就会重试
                System.out.println(sendResult);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        producer.shutdown();
    }


}
