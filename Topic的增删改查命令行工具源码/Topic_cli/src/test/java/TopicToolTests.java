
import com.myTopicTool.TopicCreationManager;
import com.myTopicTool.TopicDeletionManager;
import com.myTopicTool.TopicQueryManager;
import com.myTopicTool.TopicUpdateManager;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;


public class TopicToolTests {

    @Test
    public void testCreateTopic() throws IOException {
        String[] args = {"-topicName", "test1Topic", "-unflushThreshold", "1000", "-numPartitions", "3", "-brokerId", "1", "-createUser", "testUser"};
        TopicCreationManager.main(args);
        // Additional assertions or verifications if needed
    }

    @Test
    public void testDeleteTopic() {
        String[] args = {"-topicName", "testTopic", "-brokerId", "1"};
        TopicDeletionManager.main(args);
        // Additional assertions or verifications if needed
    }

    @Test
    public void testUpdateTopic() {
        String[] args = {"-topicName", "testTopic", "-unflushInterval", "2000", "-numPartitions", "5", "-modifyUser", "testUser", "-brokerId", "1"};
        TopicUpdateManager.main(args);
        // Additional assertions or verifications if needed
    }

    @Test
    public void testQueryTopic() {
        String[] args = {"-topicName", "testTopic", "-topicStatusId", "0", "-brokerId", "1", "-deleteWhen", "null", "-deletePolicy", "null", "-numPartitions", "3"};
        TopicQueryManager.main(args);
        // Additional assertions or verifications if needed
    }
}
