package stack.edu.Demo.samples.aws.sqs.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tip: Here we can use Spring Data JPA to make our lives easier.
 */
@Validated
@Repository
public class CustomerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepository.class);

    /**
     * Just a simple In-Memory Database
     */
    private final Map<UUID, Customer> DATABASE = new ConcurrentHashMap<>();

    public Customer save(@Valid Customer customer) {
        LOGGER.info(
            "Persisting a new customer into database: {}", customer
        );
        DATABASE.put(customer.getId(), customer);
        return customer;
    }

    public Customer findById(UUID id) {
        return DATABASE.get(id);
    }

    public List<Customer> findAll() {
        return new ArrayList<>(
                DATABASE.values().stream()
                        .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                        .toList()
        );
    }

    public void deleteAll() {
        LOGGER.info(
            "Deleting all customers from database..."
        );
        DATABASE.clear();
    }

    public int count() {
        return DATABASE.size();
    }
}
