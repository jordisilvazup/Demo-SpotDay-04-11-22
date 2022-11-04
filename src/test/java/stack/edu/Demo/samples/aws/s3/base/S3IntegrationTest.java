package stack.edu.Demo.samples.aws.s3.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers @DirtiesContext
public class S3IntegrationTest {
    private static DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

    @Container
    public static LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(S3);

    /**
     * Just configures Localstack's S3 server endpoint in the application
     */
    @DynamicPropertySource
    static void registerS3Properties(DynamicPropertyRegistry registry) {
        registry.add("cloud.aws.s3.endpoint",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(S3).toString());
    }
}
