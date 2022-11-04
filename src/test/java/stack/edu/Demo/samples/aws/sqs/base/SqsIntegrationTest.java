package stack.edu.Demo.samples.aws.sqs.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

/**
 * Base class responsible for starting Localstack and configuring it into the application
 * before tests are executed
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(SqsTestConfig.class)
@Testcontainers @DirtiesContext
public abstract class SqsIntegrationTest {

    private static DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

    @Container
    public static LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
                                                                    .withServices(SQS);

    /**
     * Just configures Localstack's SQS server endpoint in the application
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("cloud.aws.sqs.endpoint",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(SQS).toString());
    }

}
