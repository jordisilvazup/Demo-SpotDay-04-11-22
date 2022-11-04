package stack.edu.Demo.samples.aws.s3.user;

import com.amazonaws.services.s3.AmazonS3Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import stack.edu.Demo.samples.aws.s3.base.S3IntegrationTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CreateImageProfileUserControllerTest extends S3IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("userRepositoryS3")
    private UserRepository repository;

    @Autowired
    private AmazonS3Client s3Client;

    @Value("classpath:/uploads/spring.png")
    private Resource fileToUpload;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;
    private static final String DIRECTORY = "img/";


    @BeforeEach
    void setUp() {
        repository.deleteAll();
        s3Client.createBucket(bucket);
    }

    @AfterEach
    void tearDown() {
        s3Client.deleteObject(bucket, DIRECTORY.concat(fileToUpload.getFilename()));
        s3Client.deleteBucket(bucket);
    }

    @Test
    @DisplayName("must register an image for a user")
    void t1() throws Exception {
        //scenary
        User user = new User("Jordi H. Silva", "jordi.silva@zup.com.br");
        repository.save(user);

        MockMultipartFile payload = new MockMultipartFile(
                "image",
                fileToUpload.getFilename(),
                "multipart/form-data",
                fileToUpload.getInputStream()
        );

        //action and validation
        mockMvc.perform(
                multipart(
                        "/api/v1/users/{id}/images",
                        user.getId()
                ).file(payload)
        ).andExpect(
                status().isOk()
        );


        Optional<User> optionalUser = repository.findById(user.getId());

        assertThat(optionalUser)
                .isNotEmpty()
                .hasValueSatisfying(usr -> {

                    Image imageProfile = usr.getImageProfile();

                    assertThat(imageProfile.getKeyStorage())
                            .contains(fileToUpload.getFilename());

                    assertThat(imageProfile)
                            .asString()
                            .contains(bucket, fileToUpload.getFilename());
                });

    }

    @Test
    @DisplayName("should not register an empty image for a user")
    void t2() throws Exception {
        //scenary
        User user = new User("Jordi H. Silva", "jordi.silva@zup.com.br");
        repository.save(user);

        MockMultipartFile payload = new MockMultipartFile(
                "otherName",
                fileToUpload.getFilename(),
                "multipart/form-data",
                fileToUpload.getInputStream()
        );

        //action and validation
        mockMvc.perform(
                multipart(
                        "/api/v1/users/{id}/images",
                        user.getId()
                ).file(payload)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")),
                jsonPath("$.title", is("Constraint Violation")),
                jsonPath("$.status", is(400)),
                jsonPath("$.violations", hasSize(1)),
                jsonPath("$.violations", containsInAnyOrder(
                                violation("image", "must not be null")
                        )
                )
        );


    }

    @Test
    @DisplayName("should not register an invalid image")
    void t3() throws Exception {
        //scenary
        User user = new User("Jordi H. Silva", "jordi.silva@zup.com.br");
        repository.save(user);


        MockMultipartFile payload = new CustomMockMultipartFile(
                "image",
                InputStream.nullInputStream()
        );

        //action and validation
        mockMvc.perform(
                multipart(
                        "/api/v1/users/{id}/images",
                        user.getId()
                ).file(payload)
        ).andExpect(
                status().isInternalServerError()
        );


    }

    @Test
    @DisplayName("should not register an image for a non-existent user")
    void t4() throws Exception {
        //scenary
        MockMultipartFile payload = new MockMultipartFile(
                "image",
                fileToUpload.getFilename(),
                "multipart/form-data",
                fileToUpload.getInputStream()
        );

        //action and validation
        mockMvc.perform(
                multipart(
                        "/api/v1/users/{id}/images",
                        UUID.randomUUID()
                ).file(payload)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.status", is(404)),
                jsonPath("$.title", is("Not Found")),
                jsonPath("$.detail", is("User not exists"))

        );


    }

    private Map<String, Object> violation(String field, String message) {
        return Map.of(
                "field", field,
                "message", message
        );
    }

    static class CustomMockMultipartFile extends MockMultipartFile {

        public CustomMockMultipartFile(String name, InputStream contentStream) throws IOException {
            super(name, contentStream);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            throw new IOException();
        }
    }
}