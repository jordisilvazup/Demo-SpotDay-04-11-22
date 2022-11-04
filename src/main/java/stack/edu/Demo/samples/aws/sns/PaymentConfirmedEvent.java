package stack.edu.Demo.samples.aws.sns;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentConfirmedEvent(
        @NotNull UUID id,
        @NotEmpty @Size(max = 120) String description,
        @NotNull @PositiveOrZero Double amount,
        @NotNull @Past LocalDateTime createdAt
) {

}
