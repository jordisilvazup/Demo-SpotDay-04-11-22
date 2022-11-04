package stack.edu.Demo.samples.aws.sqs;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class CreateNewProductMessageSqsProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewProductMessageSqsProducer.class);

    private final String queueName;
    private final QueueMessagingTemplate sqsTemplate;

    public CreateNewProductMessageSqsProducer(QueueMessagingTemplate sqsTemplate,
                                              @Value("${samples.aws.sqs.producer-queue}") String queueName) {
        this.sqsTemplate = sqsTemplate;
        this.queueName = queueName;
    }

    /**
     * Send a new product message to SQS queue
     */
    public void send(@Valid NewProductMessage message) {

        /**
         * Tip: you can write your business logic here before sending the message to SQS
         */
        if (message == null) {
            throw new IllegalArgumentException("message can not be null");
        }

        LOGGER.info("Sending a new product message to SQS queue: {}", message);
        sqsTemplate
            .convertAndSend(queueName, message);
    }

}
