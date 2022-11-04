package stack.edu.Demo.samples.aws.s3.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tip: Here we can use Spring Data JPA to make our lives easier.
 */
@Repository
public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Just a simple In-Memory Database
     */
    private final Map<UUID, User> DATABASE = new ConcurrentHashMap<>();

    public User save(User user) {
        LOGGER.info(
                "Persisting a new user into database..."
        );
        DATABASE.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(DATABASE.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(
                DATABASE.values()
                        .stream()
                        .toList()
        );
    }

    public void deleteAll() {
        LOGGER.info(
                "Deleting all user from database..."
        );
        DATABASE.clear();
    }

    public int count() {
        return DATABASE.size();
    }
}
