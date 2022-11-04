package stack.edu.Demo.samples.aws.dynamodb.model;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@EnableScan
@Repository("premiumCustomerRepository")
public interface CustomerRepository extends CrudRepository<Customer, UUID> {
}
