package stack.edu.Demo;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.FilterType;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import static org.springframework.context.annotation.ComponentScan.Filter;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@EnableKafka
/**
 * Ignores DynamoDB Repositories when scanning for JPA Repositories
 * 
 * IMPORTANT: if you don't use Spring Data JPA you can remove this code, or, 
 *            if necessary, you can adapt it to your project.
 */
@EnableJpaRepositories(excludeFilters = {
    @Filter(type = FilterType.ANNOTATION, classes = { EnableScan.class })
})
@SpringBootApplication
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class) // Needed by Zalando Problem lib
public class SpotdaydemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotdaydemoApplication.class, args);
	}

}
