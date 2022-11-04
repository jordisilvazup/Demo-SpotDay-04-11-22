package stack.edu.Demo.samples.aws.s3.user;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.io.IOException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ImageRequest {
    @NotNull
    private MultipartFile image;

    public ImageRequest(MultipartFile image) {
        this.image = image;
    }


    public MultipartFile getImage() {
        return image;
    }

    public UploadPhotoRequest toUploadPhotoRequest() {
        try {
            return new UploadPhotoRequest(image.getInputStream(), image.getOriginalFilename());
        } catch (IOException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
