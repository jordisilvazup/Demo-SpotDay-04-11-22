package stack.edu.Demo.samples.aws.s3.user;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static java.lang.String.format;

@Component
public class S3PhotoStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3PhotoStorage.class);
    private final AmazonS3Client s3Client;
    private final String bucket;
    private static final String DIRECTORY = "img/";


    public S3PhotoStorage(
            AmazonS3Client s3Client,
            @Value("${cloud.aws.s3.bucket-name}") String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }


    public URL getUrlImage(String nameObject) {
        boolean exits = s3Client.doesObjectExist(bucket, nameObject);

        if (!exits) {
            LOGGER.error("the object: {} does not exist", nameObject);
            throw new IllegalArgumentException("Object not exist!");
        }
        LOGGER.info("Getting the url of the object: {}", nameObject);

        return s3Client.getUrl(bucket, nameObject);
    }

    public void delete(String nameObject) {
        boolean exits = s3Client.doesObjectExist(bucket, nameObject);

        if (!exits) {
            LOGGER.error("the {} does not exist", nameObject);
            throw new IllegalArgumentException("Object not exist!");
        }

        LOGGER.info("Deleting Object from S3");
        s3Client.deleteObject(bucket, nameObject);
    }


    public UploadPhotoResult upload(UploadPhotoRequest uploadPhotoRequest) {
        String key = format("%s%s", DIRECTORY, uploadPhotoRequest.getName());
        LOGGER.info("Persisting object:{} in S3", key);

        try (var photo = uploadPhotoRequest.getData()) {
            s3Client.putObject(bucket, key, photo, new ObjectMetadata());
            s3Client.setObjectAcl(bucket, key, CannedAccessControlList.PublicRead); //enable public access to read
            URL url = s3Client.getUrl(bucket, key);
            return new UploadPhotoResult(key, url);

        } catch (IOException e) {
            throw new IllegalArgumentException("Problem to upload the file to S3", e);
        }

    }

}
