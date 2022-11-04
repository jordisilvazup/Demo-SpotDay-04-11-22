package stack.edu.Demo.samples.aws.sqs;

import stack.edu.Demo.samples.aws.sqs.model.Customer;
import stack.edu.Demo.samples.aws.sqs.model.CustomerRepository;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * Simple condition to avoid initializing this listener when running integration tests that don't
 * care about it.
 *
 * Unfortunately, this is necessary because Spring Cloud tries to resolve the @SqsListener's queue URL
 * on startup, and if there's no SQS server up and running it crashes the application.
 */
@ConditionalOnProperty(
        name = "cloud.aws.sqs.listener.auto-startup", havingValue = "true"
)
@Component
public class CustomerCreatedEventSqsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCreatedEventSqsConsumer.class);

    private final CustomerRepository repository;

    public CustomerCreatedEventSqsConsumer(CustomerRepository repository) {
        this.repository = repository;
    }

    @SqsListener(
            value = "${samples.aws.sqs.consumer-queue}",
            deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS
    )
    public void receive(CustomerCreatedEvent event, @Header("MessageId") String messageId) {

        LOGGER.info(
            "Receiving a CustomerCreatedEvent (MessageId=\"{}\") from SQS queue: {}",
                messageId, event
        );

        // converts to domain model and invokes the business logic
        Customer customer = event.toModel();
        repository.save(customer);
    }

    /**
     * This is how we can handle errors with @SqsListener, and as you can see, it's very
     * similar to Controller Advices
     */
    @MessageExceptionHandler({
            ConstraintViolationException.class
    })
    public void handleOnError(ConstraintViolationException exception,
                              @Payload CustomerCreatedEvent event,
                              @Headers Map<String, String> headers) {
        LOGGER.error(
                "It was not possible to consume the message with messageId={} (ApproximateReceiveCount ={}): {}",
                headers.get("MessageId"),
                headers.get("ApproximateReceiveCount"),
                event,
                exception
        );

        // TODO: write your error handling logic here...
        // TODO: also, you can annotate this method with @SendTo("myQueue-DLQ") to forward the message to another queue
    }

}
