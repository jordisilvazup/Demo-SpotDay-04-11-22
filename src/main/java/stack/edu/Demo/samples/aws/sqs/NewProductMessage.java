package stack.edu.Demo.samples.aws.sqs;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class NewProductMessage {

    @NotNull
    private UUID id;

    @NotEmpty
    @Size(max = 120)
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;

    @Past
    @NotNull
    private LocalDateTime createdAt;

    /**
     * Tip: Although optional, we can give some tips about the constraints of
     * constructor's arguments using Bean Validation's annotations.
     */
    public NewProductMessage(@NotEmpty @Size(max = 120) String name,
                             @NotNull @Positive BigDecimal price,
                             @Past @NotNull LocalDateTime createdAt) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "NewProductMessage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}
