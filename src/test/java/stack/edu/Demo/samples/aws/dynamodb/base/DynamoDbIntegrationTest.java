package stack.edu.Demo.samples.aws.dynamodb.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

/**
 * Base class responsible for starting Localstack and configuring it into the application
 * before tests are executed
 */
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers @DirtiesContext
public abstract class DynamoDbIntegrationTest {

    private static DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

    @Container
    public static LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
                                                                    .withServices(DYNAMODB);

    /**
     * Just configures Localstack's server endpoint in the application
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // enables dynamodb's schema auto-creation
        registry.add("spring.data.dynamodb.entity2ddl.auto", () -> "create-drop");
        // configures the localstack's endpoint
        registry.add("amazon.aws.dynamodb.endpoint",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(DYNAMODB).toString());
    }

}
