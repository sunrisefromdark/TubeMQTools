# TubeMQTool使用文档

该命令行工具允许您通过命令行与 TubeMQ 交互，进行消息生产和消费操作。

## 用法

```
bashCopy code
./tubeMQTool.sh [options]
```

## 选项

- `-h`, `--help`: 显示帮助信息并退出
- `-J`, `--java-options`: 设置 Java 选项（例如，`-Xmx512m`）
- `-producer`: 执行消息生产者操作
- `-consumer`: 执行消息消费者操作
- `-message <content>`: 指定消息内容（仅适用于生产者操作）
- `-topicName <topic>`: 指定主题名称
- `-masterServers <servers>`: 指定主服务器列表，以逗号分隔
- `-groupName <group>`: 指定消费者组名称（仅适用于消费者操作）

## 示例

### 生产消息

```
bashCopy code
./tubeMQTool.sh -producer -message "Hello, TubeMQ!" -topicName "myTopic" -masterServers "127.0.0.1:8080,127.0.0.2:8080"
```

此命令将发送消息 "Hello, TubeMQ!" 到名为 "myTopic" 的主题。

### 消费消息

```
bashCopy code
./tubeMQTool.sh -consumer -topicName "myTopic" -masterServers "127.0.0.1:8080,127.0.0.2:8080" -groupName "myGroup"
```

此命令将以 "myGroup" 消费者组的身份从主题 "myTopic" 中消费消息。

## 注意事项

- 在执行命令前，请确保已正确配置 Java 执行路径（`JAVA_CMD`）和类路径（`CLASS_PATH`）。
- 提供 `-J` 或 `--java-options` 选项以设置 Java 选项。如果未提供，默认选项为 `-Xmx512m -Duser.timezone=UTC`。
- 在执行生产者操作时，必须提供 `-message`、`-topicName` 和 `-masterServers` 选项。
- 在执行消费者操作时，必须提供 `-topicName`、`-masterServers` 和 `-groupName` 选项。

如果有其他疑问，请使用 `-h` 或 `--help` 选项查看帮助信息。