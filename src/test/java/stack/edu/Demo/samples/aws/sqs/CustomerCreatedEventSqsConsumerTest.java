package stack.edu.Demo.samples.aws.sqs;

import stack.edu.Demo.samples.aws.sqs.base.SqsIntegrationTest;
import stack.edu.Demo.samples.aws.sqs.model.CustomerRepository;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Here we start the listener on startup, and we guarantee that
 * it is stopped in the end of all tests by closing the application context (thanks to @DirtiesContext)
 */
@TestPropertySource(properties = {
        "cloud.aws.sqs.listener.auto-startup = true"
})
class CustomerCreatedEventSqsConsumerTest extends SqsIntegrationTest {

    @Autowired
    private QueueMessagingTemplate sqsTemplate;

    @Autowired
    private AmazonSQSAsync SQS;

    @Value("${samples.aws.sqs.consumer-queue}")
    private String consumerQueueName;

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        SQS.purgeQueue(new PurgeQueueRequest(consumerQueueName));
    }

    @Test
    @DisplayName("should consume an event from SQS queue")
    public void t1() {
        // scenario
        CustomerCreatedEvent event = new CustomerCreatedEvent(
                UUID.randomUUID(),
                "Rafael Ponte",
                "+5585988776655",
                LocalDateTime.now()
        );

        sqsTemplate
            .convertAndSend(consumerQueueName, event);

        // action
        // ...is async, so it will be performed by our SQS listener

        // validation
        await().atMost(3, SECONDS).untilAsserted(() -> {
            assertThat(numberOfMessagesInQueue()).isEqualTo(0);
            assertThat(numberOfMessagesNotVisibleInQueue()).isEqualTo(0);
            assertThat(repository.findAll())
                    .hasSize(1)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(event.toModel());
        });
    }

    @Test
    @DisplayName("should not consume an event from SQS queue when the event is invalid")
    public void t2() {
        // scenario
        CustomerCreatedEvent invalidEvent = new CustomerCreatedEvent(
                UUID.randomUUID(), null, null, null
        );

        sqsTemplate
            .convertAndSend(consumerQueueName, invalidEvent);

        // action
        // ...is async, so it will be performed by our SQS listener

        // validation
        await().atMost(3, SECONDS).untilAsserted(() -> {
            assertThat(repository.count()).isEqualTo(0);
            assertThat(numberOfMessagesInQueue()).isEqualTo(0);
            assertThat(numberOfMessagesNotVisibleInQueue()).isEqualTo(1); // messages with errors stay not-visible for 30s
        });
    }

    private Integer numberOfMessagesInQueue() {
        GetQueueAttributesResult attributes = SQS
                .getQueueAttributes(consumerQueueName, of("All"));

        return Integer.parseInt(
                attributes.getAttributes().get("ApproximateNumberOfMessages")
        );
    }

    private Integer numberOfMessagesNotVisibleInQueue() {
        GetQueueAttributesResult attributes = SQS
                .getQueueAttributes(consumerQueueName, of("All"));

        return Integer.parseInt(
            attributes.getAttributes().get("ApproximateNumberOfMessagesNotVisible")
        );
    }
}