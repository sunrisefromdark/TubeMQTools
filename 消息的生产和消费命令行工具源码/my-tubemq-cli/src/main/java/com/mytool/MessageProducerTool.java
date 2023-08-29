package com.mytool;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.inlong.tubemq.client.config.TubeClientConfig;
import org.apache.inlong.tubemq.client.exception.TubeClientException;
import org.apache.inlong.tubemq.client.factory.MessageSessionFactory;
import org.apache.inlong.tubemq.client.factory.TubeSingleSessionFactory;
import org.apache.inlong.tubemq.client.producer.MessageProducer;
import org.apache.inlong.tubemq.client.producer.MessageSentResult;
import org.apache.inlong.tubemq.corebase.Message;
import org.apache.inlong.tubemq.corebase.cluster.MasterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageProducerTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducerTool.class);

    @SneakyThrows
    public static void produceMessage(String masterHostAndPorts, String topic, String message) {
        // 创建 MasterInfo 对象，用于连接 TubeMQ 服务器
        MasterInfo masterInfo = new MasterInfo(masterHostAndPorts);

        // 创建 TubeClientConfig 对象，配置 TubeMQ 客户端
        final TubeClientConfig clientConfig = new TubeClientConfig(masterInfo);

        // 使用 TubeClientConfig 创建单链接消息会话工厂
        final MessageSessionFactory messageSessionFactory = new TubeSingleSessionFactory(clientConfig);
        // 声明消息生产者
        MessageProducer messageProducer = null;

        try {
            // 将消息内容 message 转换为字节数组 bodyData
            byte[] bodyData = StringUtils.getBytesUtf8(message);

            // 向指定的 topic 发布消息
            messageProducer = messageSessionFactory.createProducer();
            messageProducer.publish(topic);

            // 创建 TubeMQ 的消息对象 tubeMessage，并设置消息内容和主题
            Message tubeMessage = new Message(topic, bodyData);

            // 创建日期格式化对象 dtf，用于将当前时间格式化为指定的格式
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

            // 在消息的系统头部中添加一个自定义的属性 "test"，值为当前时间的格式化字符串
            tubeMessage.putSystemHeader("test", dtf.format(LocalDateTime.now()));

            // 使用消息生产者发送消息
            MessageSentResult result = messageProducer.sendMessage(tubeMessage);
            if (result.isSuccess()) {
                System.out.println("Message sent successfully: " + message);
                System.exit(0);
            } else {
                // 记录详细错误日志
                LOGGER.error("Failed to send message: {}", message);
                // 可以在这里实现适当的错误处理，比如重试或记录日志
            }
        } catch (TubeClientException e) {
            LOGGER.error("Failed to send message: ", e);
        } finally {
            // 在外部的 try-catch 块中关闭消息生产者，以处理异常
            if (messageProducer != null) {
                try {
                    messageProducer.shutdown();
                } catch (TubeClientException e) {
                    LOGGER.error("Failed to close message producer: ", e);
                }
            }
        }
    }
}
