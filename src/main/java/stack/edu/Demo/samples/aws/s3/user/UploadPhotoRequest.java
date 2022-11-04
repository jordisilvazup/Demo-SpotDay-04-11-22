package stack.edu.Demo.samples.aws.s3.user;

import java.io.InputStream;

public class UploadPhotoRequest {
    private final InputStream data;
    private final String name;

    public UploadPhotoRequest(InputStream data, String name) {
        this.data = data;
        this.name = name;
    }

    public InputStream getData() {
        return data;
    }

    public String getName() {
        return name;
    }
}
