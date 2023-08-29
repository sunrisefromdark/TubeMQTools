import com.mytool.MessageProducerTool;
import org.junit.Test;

public class MessageProducerToolTest {

    @Test
    public void testProduceMessage() {
        String masterServers = "127.0.0.1:8715";
        String topicName = "demo";
        String message = "Hello, this is a test message.";

        // 调用生产消息的方法
        MessageProducerTool.produceMessage(masterServers, topicName, message);

        // 这里可以添加断言来验证生产消息的结果，例如是否有预期的日志输出等。
    }
}
