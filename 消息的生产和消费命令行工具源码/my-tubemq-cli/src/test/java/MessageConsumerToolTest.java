import com.mytool.MessageConsumerTool;
import org.apache.inlong.tubemq.client.consumer.PullMessageConsumer;
import org.apache.inlong.tubemq.client.exception.TubeClientException;
import org.apache.inlong.tubemq.client.factory.MessageSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageConsumerToolTest {

    @Mock
    private MessageSessionFactory sessionFactoryMock;

    @Mock
    private PullMessageConsumer consumerMock;

    @InjectMocks
    private MessageConsumerTool consumerTool;

    @Before
    public void setUp() throws Exception {
        when(sessionFactoryMock.createPullConsumer(any())).thenReturn(consumerMock);
    }

    @Test
    public void testConsumeMessages() throws InterruptedException, TubeClientException {
        String masterServers = "127.0.0.1:8715";
        String topicName = "demo";
        String groupName = "myGroup";

        CountDownLatch latch = new CountDownLatch(1);

        // Configure mocks and behavior
        when(consumerMock.subscribe(eq(topicName), any())).thenAnswer(invocation -> {
            latch.countDown();
            return null;
        });

        // Start the test
        Thread consumerThread = new Thread(() -> {
            try {
                consumerTool.consumeMessages(masterServers, topicName, groupName);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        consumerThread.start();

        // Wait for subscription to complete
        latch.await();

        // Add more behavior and verification here as needed

        // Interrupt and join the consumer thread
        consumerThread.interrupt();
        consumerThread.join();
    }
}
