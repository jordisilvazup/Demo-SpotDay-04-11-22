package stack.edu.Demo.samples.aws.dynamodb;

import stack.edu.Demo.samples.aws.dynamodb.model.Customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public record NewCustomerRequest(
        @NotEmpty @Size(max = 60) String name,
        @NotEmpty @Size(max = 60) @Email String email
) {

    /**
     * Converts this DTO to domain model
     */
    public Customer toModel() {
        return new Customer(name, email);
    }

}
