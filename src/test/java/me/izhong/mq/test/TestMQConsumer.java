package me.izhong.mq.test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TestMQConsumer {

    private static String rocketMqAddr = "10.10.51.212:9876";
    public static final AtomicLong receiveMsgCount = new AtomicLong(0); //计数器

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group_test2");
        consumer.setNamesrvAddr(rocketMqAddr);


//		consumer.setConsumeMessageBatchMaxSize(10);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//		consumer.setMaxReconsumeTimes(3);
        consumer.subscribe("topic_test2", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到报文："+String.valueOf(receiveMsgCount.addAndGet(1)));
                try {
                    System.out.println("msgs的长度" + msgs.size());
                    System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
                    for (MessageExt msg : msgs) {
                        String msgbody = new String(msg.getBody(), "utf-8");
                        if (msgbody.equals("RocketMQ4")) {
                            System.out.println("======错误=======");
                            int a = 1 / 0;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(msgs.get(0).getReconsumeTimes()==3){
                        //记录日志
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;// 成功
                    }else{
                        System.out.println("重发");
//						return null;
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;// 重试
                    }
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;// 成功
            }
        });

        consumer.start();

        System.out.println("Consumer Started.");
    }


}
