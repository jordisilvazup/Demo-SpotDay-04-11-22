package stack.edu.Demo.samples.aws.s3.user;

import java.net.URL;

public class UploadPhotoResult {
    private final String key;
    private final URL imageURl;

    public UploadPhotoResult(String key, URL imageURl) {
        this.key = key;
        this.imageURl = imageURl;
    }

    public Image toModel(){
        return new Image(key,imageURl);
    }

    public String getKey() {
        return key;
    }

    public URL getImageURl() {
        return imageURl;
    }

    @Override
    public String toString() {
        return "UploadPhotoResult{" +
                "key='" + key + '\'' +
                ", imageURl=" + imageURl +
                '}';
    }
}
