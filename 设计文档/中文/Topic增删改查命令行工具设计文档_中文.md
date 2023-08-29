[TOC]



# 						TopicTool 设计文档



### 用途

TopicTool` 类是管理消息系统中主题的命令行工具。它提供创建、删除、更新和查询主题的功能。它使用 `com.myTopicTool` 包中的各种其他类来执行这些操作。

### 方法

- main(String[]args)`： 程序的入口点。使用 Apache Commons CLI 库解析命令行参数，并调用方法执行请求的主题操作。
- `loadAllConfig()`： 为所有与主题相关的操作从属性文件中加载配置。

### 依赖关系

- `TopicCreationManager`： 处理主题的创建。
- `TopicDeletionManager`： 处理主题的删除。
- `TopicUpdateManager`： 处理主题的更新。
- `TopicQueryManager`： 处理主题的查询。



## UpdateTopicParameters 类

### 描述 

`UpdateTopicParameters` 类封装了更新主题的参数。它允许指定要更新的主题的不同属性，包括是否接受发布、是否接受订阅、未刷新阈值、分区数等。

### 属性

1. `topicName`（字符串）：要更新的主题的名称。
2. `brokerId`（整数）：主题所在的经纪人的ID。
3. `modifyUser`（字符串）：执行修改操作的用户。
4. `acceptPublish`（布尔值，可选）：是否接受发布消息到该主题。
5. `acceptSubscribe`（布尔值，可选）：是否接受从该主题订阅消息。
6. `unflushThreshold`（整数，可选）：未刷新阈值，即未刷新消息的最大数量。
7. `numPartitions`（整数，可选）：主题的分区数。
8. `deletePolicy`（字符串，可选）：删除策略。
9. `unflushInterval`（整数，可选）：未刷新消息的时间间隔。

### 方法

1. `setAcceptPublish(Boolean acceptPublish)`
   设置是否接受发布消息到主题。
2. `setAcceptSubscribe(Boolean acceptSubscribe)`
   设置是否接受从主题订阅消息。
3. `setUnflushThreshold(Integer unflushThreshold)`
   设置未刷新阈值。
4. `setNumPartitions(Integer numPartitions)`
   设置主题的分区数。
5. `setDeletePolicy(String deletePolicy)`
   设置删除策略。
6. `setUnflushInterval(Integer unflushInterval)`
   设置未刷新消息的时间间隔。
7. `getTopicName()`
   获取要更新的主题的名称。
8. `getBrokerId()`
   获取主题所在的经纪人的ID。
9. `getModifyUser()`
   获取执行修改操作的用户。
10. `isAcceptPublish()`
    是否接受发布消息到主题。
11. `getNumPartitions()`
    获取主题的分区数。
12. `getDeletePolicy()`
    获取删除策略。
13. `getUnflushThreshold()`
    获取未刷新阈值。
14. `getUnflushInterval()`
    获取未刷新消息的时间间隔。
15. `isAcceptSubscribe()`
    是否接受从主题订阅消息。



## TopicUpdateManager 类

### 描述

 `TopicUpdateManager` 类负责更新主题的属性。它通过与系统 API 进行交互，实现了修改主题属性的操作。

### 方法

1. `updateTopic(UpdateTopicParameters params) throws IOException`
   更新主题的属性，将参数传递给 `modifyTopic` 方法。
2. `modifyTopic(UpdateTopicParameters params) throws IOException`
   根据提供的参数，构建 API 请求并发送给系统，以更新主题属性。
3. `sendApiRequest(String apiUrl, Map<String, String> apiParams) throws IOException`
   向指定的 URL 发送 API 请求，使用提供的参数，并处理响应。
4. `loadConfig()`
   从文件（`config.properties`）加载配置属性，用于 API URL 和认证令牌。



## TopicCreationManager类

### 描述

 `TopicCreationManager` 类负责创建和管理主题。它与系统 API 进行交互，以创建新主题、配置其属性并监控主题创建过程。

### 方法

1. `createAndPollTopic(String topicName, int brokerId, String createUser) throws IOException`
   此方法创建一个新主题并监控创建过程。内部调用 `createTopic` 和 `pollForTopicCreation` 方法。
2. `createTopic(String topicName, int brokerId, String createUser) throws IOException`
   通过发送适当的 API 请求和指定的参数创建一个新主题。
3. `pollForTopicCreation(String topicName)`
   反复查询系统，检查指定的主题是否已成功创建。
4. `sendApiRequest(String apiUrl, Map<String, String> apiParams) throws IOException`
   向指定的 URL 发送 API 请求，使用提供的参数。
5. `loadConfig()`
   从文件（`config.properties`）加载配置属性，用于 API URL 和认证令牌。



## TopicDeletionManager类

### 描述

 `TopicDeletionManager` 类处理主题的删除和移除。它提供了不同类型的主题删除操作的方法。

### 方法

1. `deleteTopic(String topicName, int brokerId, String deleteType, String modifyUser) throws IOException`
   基于指定的删除类型，启动主题删除过程。相应地调用不同的删除方法。
2. `softDeleteTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException`
   对主题执行软删除操作，先禁用发布和订阅，然后触发重试机制。
3. `redoDeletedTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException`
   启动重做已删除主题的过程，通过发送具有相关参数的 API 请求。
4. `hardRemoveTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException`
   通过发送 API 请求执行主题的硬删除，并在必要时进行重试。
5. `performWithRetry(String apiUrl, Map<String, String> apiParams, String operationName)`
   使用重试逻辑执行 API 请求，相应地处理响应或错误。



## TopicQueryManager类

### 描述 

`TopicQueryManager` 类负责使用系统 API 查询主题信息。它提供了一个查询特定主题信息的方法。

### 方法

1. `queryTopicInfo(String topicName) throws IOException`
   查询系统以获取关于指定主题的信息，并返回查询结果。
2. `sendApiRequest(String apiUrl, Map<String, String> apiParams) throws IOException`
   向指定的 URL 发送 API 请求，使用提供的参数，并处理响应。
3. `loadConfig()`
   从文件（`config.properties`）加载配置属性，用于 API URL 和认证令牌。



## TopicManagerBase类

### 描述

 `TopicManagerBase` 类作为其他主题管理类的基类。它提供了子类共用的功能和属性。

### 属性

1. `apiUrl`：与系统交互的基本 API URL。
2. `confModAuthToken`：用于发出 API 请求的身份认证令牌。

### 方法

1. `buildApiUrl(String type, String method)`
   使用提供的类型和方法构建完整的 API URL。
2. `buildApiParams(String... params)`
   使用提供的名称-值对构建 API 参数。
3. `loadConfig()`
   从文件（`config.properties`）加载配置属性，用于 API URL 和认证令牌。
