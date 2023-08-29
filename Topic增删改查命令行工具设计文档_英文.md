[TOC]



# 													TopicTool Design Document

### Purpose

The `TopicTool` class serves as a command-line tool for managing topics in a messaging system. It provides functionality for creating, deleting, updating, and querying topics. It uses various other classes from the `com.myTopicTool` package to perform these operations.

### Methods

- `main(String[] args)`: The entry point of the program. Parses command-line arguments using Apache Commons CLI library and calls methods to perform requested topic operations.
- `loadAllConfig()`: Loads configuration from properties file for all topic-related operations.

### Dependencies

- `TopicCreationManager`: Handles the creation of topics.
- `TopicDeletionManager`: Handles the deletion of topics.
- `TopicUpdateManager`: Handles the updating of topics.
- `TopicQueryManager`: Handles the querying of topics.



## UpdateTopicParameters 

### Purpose

The `UpdateTopicParameters` class encapsulates the parameters required for updating a topic. It provides a clean way to pass these parameters to the `TopicUpdateManager` class for topic updates.

### Fields

- `topicName`: The name of the topic to update.
- `brokerId`: The ID of the broker associated with the topic.
- `modifyUser`: The user performing the modification.
- `acceptPublish`: Whether the topic should accept publishing (optional).
- `acceptSubscribe`: Whether the topic should accept subscribing (optional).
- `numPartitions`: The number of partitions for the topic (optional).
- `deletePolicy`: The deletion policy for the topic (optional).
- `unflushInterval`: The unflush interval for the topic (optional).

### Methods

- `setAcceptPublish(Boolean acceptPublish)`: Sets whether the topic should accept publishing.
- `setAcceptSubscribe(Boolean acceptSubscribe)`: Sets whether the topic should accept subscribing.
- `setUnflushThreshold(Integer unflushThreshold)`: Sets the unflush threshold for the topic.
- `setNumPartitions(Integer numPartitions)`: Sets the number of partitions for the topic.
- `setDeletePolicy(String deletePolicy)`: Sets the deletion policy for the topic.
- `setUnflushInterval(Integer unflushInterval)`: Sets the unflush interval for the topic.
- Accessor methods for fields such as `getTopicName()`, `getBrokerId()`, etc.



## TopicUpdateManager

### Purpose

The `TopicUpdateManager` class manages the process of updating topics. It communicates with the messaging system's API to modify topic settings.

### Methods

- `updateTopic(UpdateTopicParameters params)`: Public method for updating a topic, delegates to `modifyTopic()`.
- `modifyTopic(UpdateTopicParameters params)`: Modifies the topic's attributes using the provided parameters.
- `sendApiRequest(String apiUrl, Map<String, String> apiParams)`: Sends an API request with specified parameters.
- `loadConfig()`: Loads configuration from properties file for topic update operations.

### Dependencies

- `UpdateTopicParameters`: Holds the parameters required for updating topics.

These design documents provide an overview of the purpose, fields, methods, and relationships of the classes in your codebase, helping developers understand and work with the code more effectively.



## TopicCreationManager

### Description

 The `TopicCreationManager` class is responsible for creating and managing topics. It interacts with the system API to create new topics, configure their properties, and monitor the topic creation process.

### Methods

1. `createAndPollTopic(String topicName, int brokerId, String createUser) throws IOException`
   This method creates a new topic and monitors the creation process. It calls `createTopic` and `pollForTopicCreation` internally.
2. `createTopic(String topicName, int brokerId, String createUser) throws IOException`
   Creates a new topic by sending appropriate API requests with the specified parameters.
3. `pollForTopicCreation(String topicName)`
   Repeatedly queries the system to check if the specified topic has been successfully created.
4. `sendApiRequest(String apiUrl, Map<String, String> apiParams) throws IOException`
   Sends an API request to the specified URL with the provided parameters.
5. `loadConfig()`
   Loads configuration properties from a file (`config.properties`) for API URL and authentication token.



## TopicDeletionManager

### Description

 The `TopicDeletionManager` class handles the deletion and removal of topics. It provides methods to perform different types of deletion operations on topics.

### Methods

1. `deleteTopic(String topicName, int brokerId, String deleteType, String modifyUser) throws IOException`
   Initiates the process of topic deletion based on the specified delete type. Calls different deletion methods accordingly.
2. `softDeleteTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException`
   Performs a soft delete operation on the topic by disabling publish and subscribe, and then triggers a retry mechanism.
3. `redoDeletedTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException`
   Initiates the process of redoing a deleted topic by sending API requests with relevant parameters.
4. `hardRemoveTopicWithRetry(String topicName, int brokerId, String modifyUser) throws IOException`
   Performs a hard removal of the topic by sending API requests, and retries the operation if necessary.
5. `performWithRetry(String apiUrl, Map<String, String> apiParams, String operationName)`
   Executes an API request with retry logic and handles response or errors accordingly.



## TopicQueryManager

### Description

The `TopicQueryManager` class is responsible for querying topic information using the system API. It provides a method to query information about a specific topic.

### Methods

1. `queryTopicInfo(String topicName) throws IOException`
   Queries the system for information about the specified topic and returns the query result.
2. `sendApiRequest(String apiUrl, Map<String, String> apiParams) throws IOException`
   Sends an API request to the specified URL with the provided parameters and handles the response.
3. `loadConfig()`
   Loads configuration properties from a file (`config.properties`) for API URL and authentication token.



## TopicManagerBase

### Description

The `TopicManagerBase` class serves as a base class for other topic management classes. It provides common functionality and attributes used by the subclasses.

### Attributes

1. `apiUrl`: The base API URL for interacting with the system.
2. `confModAuthToken`: The authentication token for making API requests.

### Methods

1. `buildApiUrl(String type, String method)`
   Builds the complete API URL using the provided type and method.
2. `buildApiParams(String... params)`
   Builds API parameters using the provided name-value pairs.
3. `loadConfig()`
   Loads configuration properties from a file (`config.properties`) for API URL and authentication token.

