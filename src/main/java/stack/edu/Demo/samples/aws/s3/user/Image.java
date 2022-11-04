package stack.edu.Demo.samples.aws.s3.user;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URL;

public class Image {
    @NotBlank
    private final String keyStorage;
    @NotNull
    private final URL urlDownload;

    /**
     * Tip: Although optional, we can give some tips about the constraints of
     * constructor's arguments using Bean Validation's annotations.
     */
    public Image(@NotBlank String keyStorage, @NotNull URL urlDownload) {
        this.keyStorage = keyStorage;
        this.urlDownload = urlDownload;
    }

    public String getKeyStorage() {
        return keyStorage;
    }

    public URL getUrlDownload() {
        return urlDownload;
    }

    @Override
    public String toString() {
        return "Image{" +
                "keyStorage='" + keyStorage + '\'' +
                ", urlDownload=" + urlDownload +
                '}';
    }
}
