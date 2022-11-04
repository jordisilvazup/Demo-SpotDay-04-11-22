package stack.edu.Demo.samples.aws.s3.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

public class User {

    @NotNull
    private final UUID id;

    @NotEmpty
    @Size(max = 120)
    private final String name;

    @Email
    @NotEmpty
    @Size(max = 60)
    private final String email;


    private Image imageProfile;

    /**
     * Tip: Although optional, we can give some tips about the constraints of
     * constructor's arguments using Bean Validation's annotations.
     */
    public User(@NotEmpty @Size(max = 120) String name,
                @Email @NotEmpty @Size(max = 60) String email
    ) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;

        // TODO: asserts
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Image getImageProfile() {
        return imageProfile;
    }

    public void add(Image image) {
        this.imageProfile = image;
    }

    /**
     * Tip: Don't forget to think a little about the entity's identity, because every entity has an identity.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User author = (User) o;
        return email.equals(author.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageProfile=" + imageProfile +
                '}';
    }
}
