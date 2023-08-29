#!/bin/bash

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Set the path to the JAR file
JAR_PATH="$SCRIPT_DIR/Topic_cli-1.0-SNAPSHOT.jar"

# Java command to run the JAR
JAVA_CMD="java -jar $JAR_PATH"

# Function to display usage
display_usage() {
    echo "Usage: $0 [-create | -delete | -update | -query | -help] [options]"
    echo "Options:"
    echo "  -create     Create a topic"
    echo "              -topicName <topic_name> -brokerId <broker_id> -createUser <user>"
    echo "              [-numPartitions <num_partitions>]"
    echo "  -delete     Delete a topic"
    echo "              -topicName <topic_name> -brokerId <broker_id> -deleteType <softDelete|redo|hardDelete> -modifyUser <user>"
    echo "  -update     Update a topic"
    echo "              -topicName <topic_name> -brokerId <broker_id> -modifyUser <user>"
    echo "              [-acceptPublish <true|false>] [-acceptSubscribe <true|false>]"
    echo "              [-unflushThreshold <threshold>] [-numPartitions <num_partitions>]"
    echo "              [-deletePolicy <policy>] [-unflushInterval <interval>]"
    echo "  -query      Query a topic"
    echo "              -topicName <topic_name>"
    echo "  -help       Display this help message"
}

# Check for help flag
if [[ "$1" == "-help" ]]; then
    display_usage
    exit 0
fi

# Process the command line arguments
case "$1" in
    -create)
        shift
        $JAVA_CMD -create "$@"
        ;;
    -delete)
        shift
        $JAVA_CMD -delete "$@"
        ;;
    -update)
        shift
        $JAVA_CMD -update "$@"
        ;;
    -query)
        shift
        $JAVA_CMD -query "$@"
        ;;
    *)
        echo "Invalid option: $1"
        display_usage
        exit 1
        ;;
esac

exit 0
