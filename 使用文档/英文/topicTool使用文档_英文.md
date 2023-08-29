# 		topicTool Usage Documentation

This command-line tool allows you to perform various operations on topics using a Java JAR file.

## Usage

```
bashCopy code
./topicTool.sh [options]
```

## Options

- `-create`: Create a topic
  - `-topicName <topic_name>`: Specify the topic name
  - `-brokerId <broker_id>`: Specify the broker ID
  - `-createUser <user>`: Specify the user creating the topic
  - `-numPartitions <num_partitions>`: (Optional) Specify the number of partitions
- `-delete`: Delete a topic
  - `-topicName <topic_name>`: Specify the topic name
  - `-brokerId <broker_id>`: Specify the broker ID
  - `-deleteType <softDelete|redo|hardDelete>`: Specify the delete type
  - `-modifyUser <user>`: Specify the user modifying the topic
- `-update`: Update a topic
  - `-topicName <topic_name>`: Specify the topic name
  - `-brokerId <broker_id>`: Specify the broker ID
  - `-modifyUser <user>`: Specify the user modifying the topic
  - `-acceptPublish <true|false>`: (Optional) Specify whether to accept publishing
  - `-acceptSubscribe <true|false>`: (Optional) Specify whether to accept subscribing
  - `-unflushThreshold <threshold>`: (Optional) Specify the unflush threshold
  - `-numPartitions <num_partitions>`: (Optional) Specify the number of partitions
  - `-deletePolicy <policy>`: (Optional) Specify the delete policy
  - `-unflushInterval <interval>`: (Optional) Specify the unflush interval
- `-query`: Query a topic
  - `-topicName <topic_name>`: Specify the topic name
- `-help`: Display this help message

## Examples

### Create a Topic

```
bashCopy code
./topicTool.sh -create -topicName myTopic -brokerId 1 -createUser user1 -numPartitions 6
```

This command will create a topic named "myTopic" with broker ID 1, created by user "user1", and 6 partitions.

### Delete a Topic

```
bashCopy code
./topicTool.sh -delete -topicName myTopic -brokerId 1 -deleteType softDelete -modifyUser user1
```

This command will delete the topic "myTopic" on broker ID 1 with a soft delete operation, performed by user "user1".

### Update a Topic

```
bashCopy code
./topicTool.sh -update -topicName myTopic -brokerId 1 -modifyUser user1 -acceptPublish true -unflushThreshold 1400
```

This command will update the "myTopic" topic on broker ID 1, performed by user "user1", accepting publishing and setting an unflush threshold of 1400.

### Query a Topic

```
bashCopy code
./topicTool.sh -query -topicName myTopic
```

This command will query information about the "myTopic" topic.

## Notes

- The provided JAR file path (`JAR_PATH`) should be configured to point to the appropriate JAR file.
- When using optional parameters, enclose the parameter value in double quotes if it contains spaces.
- To display this help message, use the `-help` option.

For additional help, use the `-help` option to display the help message.