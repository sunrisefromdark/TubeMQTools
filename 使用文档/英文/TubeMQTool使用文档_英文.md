# TubeMQTool Usage Documentation

This command-line tool allows you to interact with TubeMQ through the command line for message production and consumption.

## Usage

```
bashCopy code
./tubeMQTool.sh [options]
```

## Options

- `-h`, `--help`: Show help message and exit
- `-J`, `--java-options`: Set Java options (e.g., `-Xmx512m`)
- `-producer`: Perform producer operation
- `-consumer`: Perform consumer operation
- `-message <content>`: Specify the message content (for producer operation)
- `-topicName <topic>`: Specify the topic name
- `-masterServers <servers>`: Specify the list of master servers, separated by commas
- `-groupName <group>`: Specify the consumer group name (for consumer operation)

## Examples

### Produce Message

```
bashCopy code
./tubeMQTool.sh -producer -message "Hello, TubeMQ!" -topicName "myTopic" -masterServers "127.0.0.1:8080,127.0.0.2:8080"
```

This command will send the message "Hello, TubeMQ!" to the topic named "myTopic".

### Consume Message

```
bashCopy code
./tubeMQTool.sh -consumer -topicName "myTopic" -masterServers "127.0.0.1:8080,127.0.0.2:8080" -groupName "myGroup"
```

This command will consume messages from the topic "myTopic" under the consumer group "myGroup".

## Notes

- Before executing the command, ensure that the Java execution path (`JAVA_CMD`) and class path (`CLASS_PATH`) are properly configured.
- Provide the `-J` or `--java-options` option to set Java options. If not provided, the default options are `-Xmx512m -Duser.timezone=UTC`.
- When performing producer operation, you must provide the `-message`, `-topicName`, and `-masterServers` options.
- When performing consumer operation, you must provide the `-topicName`, `-masterServers`, and `-groupName` options.

For additional help, use the `-h` or `--help` option to display the help message.