#!/bin/bash

# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 设置 Java 执行命令路径和默认选项
JAVA_CMD="java"
DEFAULT_JAVA_OPTIONS="-Xmx512m -Duser.timezone=UTC"
# 使用用户指定的 Java 选项，如果没有指定则使用默认选项
JAVA_OPTIONS="${JAVA_OPTIONS:-$DEFAULT_JAVA_OPTIONS}"

# 设置 TubeMQCommandLineTool 类的完整路径
CLASS_PATH="com.mytool.MainApplication"

# 设置 JAR 文件相对于脚本的路径
JAR_PATH="my-tubemq-cli-1.0-SNAPSHOT.jar"

# 显示帮助信息
show_help() {
    echo "Usage: $(basename "$0") [options]"
    echo "Options:"
    echo "  -h, --help            Show this help message and exit"
    echo "  -J, --java-options    Set Java options (e.g., -Xmx512m)"
    echo "  -producer             Produce a message"
    echo "  -message              Message content"
    echo "  -topicName            Topic name"
    echo "  -masterServers        Master servers"
}

# 处理命令行参数
while [[ $# -gt 0 ]]; do
    case "$1" in
        -h|--help)
            show_help
            exit 0
            ;;
        -J|--java-options)
            shift
            JAVA_OPTIONS="$1"
            ;;
        -producer)
            MODE="producer"
            shift
            ;;
        -message)
            shift
            MESSAGE="$1"
            shift
            ;;
        -topicName)
            shift
            TOPIC_NAME="$1"
            shift
            ;;
        -masterServers)
            shift
            MASTER_SERVERS="$1"
            shift
            ;;
        -consumer)
            MODE="consumer"
            shift
            ;;
        -groupName)
            shift
            GROUP_NAME="$1"
            shift
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# 检查必要的参数
if [[ -z "$JAVA_CMD" || -z "$CLASS_PATH" || -z "$JAR_PATH" ]]; then
    echo "Error: Missing required configuration. Please check JAVA_CMD, CLASS_PATH, and JAR_PATH."
    exit 1
fi

# 执行 Java 命令行工具
if [ "$MODE" == "producer" ]; then
    execute_java_tool() {
        $JAVA_CMD $JAVA_OPTIONS -cp "$SCRIPT_DIR/$JAR_PATH" $CLASS_PATH -producer -message "$MESSAGE" -topicName "$TOPIC_NAME" -masterServers "$MASTER_SERVERS"
    }
elif [ "$MODE" == "consumer" ]; then
    execute_java_tool() {
        $JAVA_CMD $JAVA_OPTIONS -cp "$SCRIPT_DIR/$JAR_PATH" $CLASS_PATH -consumer -topicName "$TOPIC_NAME" -masterServers "$MASTER_SERVERS" -groupName "$GROUP_NAME"
    }
else
    echo "Unknown mode: $MODE"
    show_help
    exit 1
fi

# 执行 Java 命令行工具
execute_java_tool

# 生产成功后，强制退出脚本
exit 0