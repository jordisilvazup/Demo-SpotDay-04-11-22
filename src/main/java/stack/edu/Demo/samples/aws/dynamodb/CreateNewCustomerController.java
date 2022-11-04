package stack.edu.Demo.samples.aws.dynamodb;

import stack.edu.Demo.samples.aws.dynamodb.model.Customer;
import stack.edu.Demo.samples.aws.dynamodb.model.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class CreateNewCustomerController {

    private final CustomerRepository repository;

    public CreateNewCustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/api/v1/premium-customers")
    public ResponseEntity<?> create(@RequestBody @Valid NewCustomerRequest request, UriComponentsBuilder uriBuilder) {

        // TODO: existsByEmail()

        Customer customer = request.toModel();
        repository.save(customer);

        URI location = uriBuilder
                        .path("/api/v1/premium-customers/{id}")
                        .buildAndExpand(customer.getId())
                        .toUri();

        return ResponseEntity
                .created(location).build();
    }
}
