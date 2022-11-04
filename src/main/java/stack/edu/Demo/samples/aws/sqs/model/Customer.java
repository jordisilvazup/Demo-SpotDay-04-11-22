package stack.edu.Demo.samples.aws.sqs.model;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Customer {

    @NotNull
    private UUID id;

    @NotEmpty
    @Size(max = 120)
    private String name;

    @NotEmpty
    @Pattern(regexp = "^\\+[1-9][0-9]\\d{1,14}")
    private String phoneNumber;

    @Past
    @NotNull
    private LocalDateTime createdAt;

    /**
     * Tip: Although optional, we can give some tips about the constraints of
     * constructor's arguments using Bean Validation's annotations.
     */
    public Customer(@NotNull UUID id,
                    @NotEmpty @Size(max = 120) String name,
                    @NotEmpty @Pattern(regexp = "^\\+[1-9][0-9]\\d{1,14}") String phoneNumber,
                    @Past @NotNull LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
