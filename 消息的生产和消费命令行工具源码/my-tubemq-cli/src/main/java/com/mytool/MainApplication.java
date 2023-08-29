package com.mytool;

import org.apache.commons.cli.*;
import org.apache.inlong.tubemq.client.exception.TubeClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        // 创建命令行解析器
        CommandLineParser parser = new DefaultParser();

        // 创建命令行选项配置
        Options options = createOptions();

        try {
            // 解析命令行参数
            CommandLine cmd = parser.parse(options, args);
            // 处理解析结果
            processCommandLineOptions(cmd, options);
        } catch (ParseException e) {
            // 解析错误时打印错误信息
            LOGGER.error("Error parsing command line: {}", e.getMessage());
            // 打印命令行工具的使用帮助
            printUsage(options);
        } catch (TubeClientException e) {
            // 记录 TubeClientException 异常
            LOGGER.error("TubeClientException: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理命令行解析结果
     * @param cmd 解析结果
     * @param options 命令行选项配置
     */
    private static void processCommandLineOptions(CommandLine cmd, Options options) throws TubeClientException {
        if (cmd.hasOption("producer")) {
            // 解析"producer"选项，获取消息内容、主题和master servers地址
            String message = cmd.getOptionValue("message");
            String topic = cmd.getOptionValue("topicName");
            String masterHostAndPorts = cmd.getOptionValue("masterServers");
            // 调用MessageProducerTool的produceMessage函数进行消息生产
            MessageProducerTool.produceMessage(masterHostAndPorts, topic, message);
        }else if (cmd.hasOption("consumer")) {
            // 解析"consumer"选项，获取主题、master servers地址和消费者组名称
            String topic = cmd.getOptionValue("topicName");
            String masterServers = cmd.getOptionValue("masterServers");
            String groupName = cmd.getOptionValue("groupName");

            // 创建一个订阅 latch，用于确保订阅完成后再执行消费任务
            CountDownLatch subscriptionLatch = new CountDownLatch(1);

            // 调用MessageConsumerTool的consumeMessages函数进行消息消费
            Runnable consumerTask = () -> {
                try {
                    MessageConsumerTool.consumeMessages(masterServers, topic, groupName);
                } catch (TubeClientException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                subscriptionLatch.countDown();
            };

            Thread consumerThread = new Thread(consumerTask);
            consumerThread.start();

            // 等待订阅完成
            try {
                subscriptionLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            // 无效的命令，打印错误信息和使用帮助
            LOGGER.error("Invalid command. Please use either -producer or -consumer.");
            printUsage(options);
        }
    }

    /**
     * 创建命令行选项的配置
     * @return 包含命令行选项的Options对象
     */
    private static Options createOptions() {
        Options options = new Options();

        // 创建"producer"选项，用于指定要生产消息
        Option producerOption = new Option("producer", false, "Produce a message");
        // 创建"consumer"选项，用于指定要消费消息
        Option consumerOption = new Option("consumer", false, "Consume messages");

        // 创建"message"选项，用于指定生产的消息内容
        Option messageOption = Option.builder("message")
                .argName("message")
                .hasArg()
                .desc("Message content")
                .build();

        // 创建"topicName"选项，用于指定消息的主题
        Option topicOption = Option.builder("topicName")
                .argName("topicName")
                .hasArg()
                .desc("Topic name")
                .build();

        // 创建"masterServers"选项，用于指定master servers地址
        Option masterServersOption = Option.builder("masterServers")
                .argName("masterServers")
                .hasArg()
                .desc("Master servers")
                .build();

        // 创建"groupName"选项，用于指定消费者组名称
        Option groupNameOption = Option.builder("groupName")
                .argName("groupName")
                .hasArg()
                .desc("Consumer group name")
                .build();

        // 将各选项添加到Options对象中
        options.addOption(producerOption);
        options.addOption(consumerOption);
        options.addOption(messageOption);
        options.addOption(topicOption);
        options.addOption(masterServersOption);
        options.addOption(groupNameOption);

        return options;
    }

    /**
     * 打印命令行工具的使用帮助
     * @param options 命令行选项配置
     */
    private static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyTool", options);
    }
}
