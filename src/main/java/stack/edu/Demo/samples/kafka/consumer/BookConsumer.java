package stack.edu.Demo.samples.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import stack.edu.Demo.samples.kafka.BookEvent;


@Component
public class BookConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookConsumer.class);
    @Qualifier("bookRepositoryKafka")
    private final BookRepository repository;

    public BookConsumer(BookRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen( @Payload BookEvent event) {
        LOGGER.info(
                "The following event has just been consumed: {}",
                event
        );

        Book book = event.toModel();
        repository.save(book);
    }


}
