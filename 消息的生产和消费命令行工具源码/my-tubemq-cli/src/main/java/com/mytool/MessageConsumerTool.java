package com.mytool;

import org.apache.commons.collections.CollectionUtils;
import org.apache.inlong.common.msg.InLongMsg;
import org.apache.inlong.tubemq.client.common.PeerInfo;
import org.apache.inlong.tubemq.client.config.ConsumerConfig;
import org.apache.inlong.tubemq.client.consumer.ConsumePosition;
import org.apache.inlong.tubemq.client.consumer.MessageListener;
import org.apache.inlong.tubemq.client.consumer.PushMessageConsumer;
import org.apache.inlong.tubemq.client.factory.MessageSessionFactory;
import org.apache.inlong.tubemq.client.factory.TubeSingleSessionFactory;
import org.apache.inlong.tubemq.corebase.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageConsumerTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducerTool.class);
    private static final AtomicInteger processedMessagesCount = new AtomicInteger(0);


    public static void consumeMessages(String masterHostAndPort, String topic, String group) throws Throwable {
        final ConsumerConfig consumerConfig = new ConsumerConfig(masterHostAndPort, group);
        consumerConfig.setConsumePosition(ConsumePosition.CONSUMER_FROM_LATEST_OFFSET);
        final MessageSessionFactory messageSessionFactory = new TubeSingleSessionFactory(consumerConfig);
        final PushMessageConsumer pushConsumer = messageSessionFactory.createPushConsumer(consumerConfig);

        // 定义消息监听器
        MessageListener messageListener = new MessageListener() {
            @Override
            public void receiveMessages(PeerInfo peerInfo, List<Message> messages) throws InterruptedException {
                for (Message message : messages) {
                    messageProcessingThread.submit(() -> processMessage(message.getData()));
                }
            }

            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void stop() {
                // 停止消息处理
            }
        };


        // 订阅主题并设置消息监听器
        pushConsumer.subscribe(topic, null, messageListener);
        pushConsumer.completeSubscribe();

        // 等待一段时间，这里使用CountDownLatch来控制示例的运行时间
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.MINUTES);

        // 关闭消费者
        pushConsumer.shutdown();
    }

    private static final ExecutorService messageProcessingThread = Executors.newFixedThreadPool(5);



    // 具体业务逻辑处理方法
    private static void processMessage(byte[] messageContent) {
        try {
//            // 解析消息内容
//            List<byte[]> parsedData = parserInLongMsg(messageContent);
//
//            System.out.println("看到这条输出，说明消息解析是没问题的");
//            // 逐个打印解析后的消息内容
//            for (byte[] data : parsedData) {
//                String message = new String(data);
//                System.out.println("Received message: " + message);
//            }
            String message = new String(messageContent);
            System.out.println("Received message: " + message);

        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", e.getMessage());
        }
    }



    public static List<byte[]> parserInLongMsg(byte[] bytes) {
        List<byte[]> originalContentByteList = new ArrayList<>();
        InLongMsg inLongMsg = InLongMsg.parseFrom(bytes);
        Set<String> attrs = inLongMsg.getAttrs();
        if (CollectionUtils.isEmpty(attrs)) {
            return originalContentByteList;
        }
        for (String attr : attrs) {
            if (attr == null) {
                continue;
            }
            Iterator<byte[]> iterator = inLongMsg.getIterator(attr);
            if (iterator == null) {
                continue;
            }
            while (iterator.hasNext()) {
                byte[] bodyBytes = iterator.next();
                if (bodyBytes == null || bodyBytes.length == 0) {
                    continue;
                }
                // agent 发送的原始用户数据
                originalContentByteList.add(bodyBytes);
            }
        }
        return originalContentByteList;
    }

    public static void main(String[] args) throws Throwable {
        String masterServers = "127.0.0.1:8715";
        String topicName = "demo";
        String groupName = "testgroup";
        consumeMessages(masterServers, topicName, groupName);
    }

}
