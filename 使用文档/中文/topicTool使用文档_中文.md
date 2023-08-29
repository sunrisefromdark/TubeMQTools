# 			topicTool命令行工具使用文档

该命令行工具允许您使用 Java JAR 文件对主题执行各种操作。

## 使用方法

```
bashCopy code
./topicTool.sh [options]
```

## 选项

- `-create`：创建主题
  - `-topicName <topic_name>`：指定主题名称
  - `-brokerId <broker_id>`：指定代理 ID
  - `-createUser <user>`：指定创建主题的用户
  - `-numPartitions <num_partitions>`：（可选）指定分区数
- `-delete`：删除主题
  - `-topicName <topic_name>`：指定主题名称
  - `-brokerId <broker_id>`：指定代理 ID
  - `-deleteType <softDelete|redo|hardDelete>`：指定删除类型
  - `-modifyUser <user>`：指定修改主题的用户
- `-update`：更新主题
  - `-topicName <topic_name>`：指定主题名称
  - `-brokerId <broker_id>`：指定代理 ID
  - `-modifyUser <user>`：指定修改主题的用户
  - `-acceptPublish <true|false>`：（可选）指定是否接受发布
  - `-acceptSubscribe <true|false>`：（可选）指定是否接受订阅
  - `-unflushThreshold <threshold>`：（可选）指定未刷新阈值
  - `-numPartitions <num_partitions>`：（可选）指定分区数
  - `-deletePolicy <policy>`：（可选）指定删除策略
  - `-unflushInterval <interval>`：（可选）指定未刷新间隔
- `-query`：查询主题
  - `-topicName <topic_name>`：指定主题名称
- `-help`：显示帮助信息

## 示例

### 创建主题

```
bashCopy code
./topicTool.sh -create -topicName myTopic -brokerId 1 -createUser user1 -numPartitions 6
```

该命令将创建一个名为 "myTopic" 的主题，代理 ID 为 1，由用户 "user1" 创建，包含 6 个分区。

### 删除主题

```
bashCopy code
./topicTool.sh -delete -topicName myTopic -brokerId 1 -deleteType softDelete -modifyUser user1
```

该命令将使用软删除操作删除主题 "myTopic"，在代理 ID 为 1 上执行，由用户 "user1" 修改。

### 更新主题

```
bashCopy code
./topicTool.sh -update -topicName myTopic -brokerId 1 -modifyUser user1 -acceptPublish true -unflushThreshold 1400
```

该命令将更新主题 "myTopic"，在代理 ID 为 1 上执行，由用户 "user1" 修改，接受发布，并将未刷新阈值设置为 1400。

### 查询主题

```
bashCopy code
./topicTool.sh -query -topicName myTopic
```

该命令将查询有关主题 "myTopic" 的信息。

## 注意事项

- 提供的 JAR 文件路径（`JAR_PATH`）应配置为指向适当的 JAR 文件。
- 在使用可选参数时，如果参数值包含空格，请将其用双引号括起来。
- 要显示此帮助信息，请使用 `-help` 选项。

如需额外帮助，请使用 `-help` 选项来显示帮助信息。