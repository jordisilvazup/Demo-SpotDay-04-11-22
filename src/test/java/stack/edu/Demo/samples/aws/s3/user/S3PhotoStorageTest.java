package stack.edu.Demo.samples.aws.s3.user;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import stack.edu.Demo.samples.aws.s3.base.S3IntegrationTest;

import java.io.IOException;
import java.net.URL;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class S3PhotoStorageTest extends S3IntegrationTest {
    @Autowired
    private S3PhotoStorage s3PhotoStorage;

    @Autowired
    private AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;

    @Value("classpath:/uploads/spring.png")
    private Resource fileToUpload;

    private String FILE_KEY;

    @BeforeEach
    void setUp() {
        s3Client.createBucket(bucket);
        this.FILE_KEY = format("img/%s", fileToUpload.getFilename());
    }

    @AfterEach
    void tearDown() {
        s3Client.deleteObject(bucket, FILE_KEY);
        s3Client.deleteBucket(bucket);
    }

    @Test
    @DisplayName("must upload the file")
    void t1() throws IOException {

        UploadPhotoRequest uploadPhotoRequest = new UploadPhotoRequest(fileToUpload.getInputStream(), fileToUpload.getFilename());
        s3PhotoStorage.upload(uploadPhotoRequest);

        boolean exist = s3Client.doesObjectExist(bucket, FILE_KEY);

        assertTrue(exist,
                "Deveria existir o arquivo");
    }


    @Test
    @DisplayName("must create the file download url")
    void t3() throws IOException {
        //scenary
        s3Client.putObject(bucket, FILE_KEY, fileToUpload.getInputStream(), new ObjectMetadata());

        // action
        URL urlDownloadObject = s3PhotoStorage.getUrlImage(FILE_KEY);

        //validation
        assertThat(urlDownloadObject)
                .isNotNull()
                .asString()
                .contains(bucket, FILE_KEY);
    }

    @Test
    @DisplayName("must not create download url for non-existing object")
    void t4() {
        //scenary
        String keyNotExist = "NOT_FOUND";

        //action and validation
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            s3PhotoStorage.getUrlImage(keyNotExist);
        });

        assertThat(exception)
                .hasMessageContaining("Object not exist!");
    }

    @Test
    @DisplayName("must delete an object")
    void t5() throws IOException {
        //scenary
        s3Client.putObject(bucket, FILE_KEY, fileToUpload.getInputStream(), new ObjectMetadata());

        //action
        s3PhotoStorage.delete(FILE_KEY);

        //validation
        assertFalse(
                s3Client.doesObjectExist(bucket, FILE_KEY)
        );

    }

    @Test
    @DisplayName("must not delete a non-existent object")
    void t6() {
        //scenary
        String keyNotExist = "NOT_FOUND";

        //action and validation
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            s3PhotoStorage.delete(keyNotExist);
        });

        assertThat(exception)
                .hasMessageContaining("Object not exist!");

    }

}