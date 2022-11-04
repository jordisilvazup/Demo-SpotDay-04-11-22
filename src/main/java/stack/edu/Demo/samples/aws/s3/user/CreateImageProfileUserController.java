package stack.edu.Demo.samples.aws.s3.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
public class CreateImageProfileUserController {
    @Qualifier("userRepositoryS3")
    private final UserRepository repository;
    private final S3PhotoStorage s3PhotoStorage;

    public CreateImageProfileUserController(UserRepository repository, S3PhotoStorage s3PhotoStorage) {
        this.repository = repository;
        this.s3PhotoStorage = s3PhotoStorage;
    }

    @PostMapping(
            value = "/api/v1/users/{id}/images",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@PathVariable UUID id, @Valid ImageRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not exists"));

        UploadPhotoRequest uploadPhotoRequest = request.toUploadPhotoRequest();
        UploadPhotoResult result = s3PhotoStorage.upload(uploadPhotoRequest);

        Image userImage = result.toModel();
        user.add(userImage);
        repository.save(user);

        return ResponseEntity.ok().build();
    }
}
