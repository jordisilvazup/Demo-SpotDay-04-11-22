package stack.edu.Demo.samples.aws.dynamodb;

import stack.edu.Demo.samples.aws.dynamodb.base.DynamoDbIntegrationTest;
import stack.edu.Demo.samples.aws.dynamodb.model.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.spring.common.MediaTypes;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CreateNewCustomerControllerTest extends DynamoDbIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    /**
     * Tip: Try always to begin with the happy-path scenario
     */
    @Test
    @DisplayName("should create a new premium-customer")
    public void t1() throws Exception {
        // scenario
        NewCustomerRequest request = new NewCustomerRequest(
                "Rafael Ponte",
                "rafael.ponte@zup.com.br"
        );

        // action
        mockMvc.perform(post("/api/v1/premium-customers")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/api/v1/premium-customers/*"))
        ;

        // validation
        // Tip: Try always to verify the side effects
        assertThat(repository.findAll())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt")
                .contains(request.toModel())
        ;
    }

    @Test
    @DisplayName("should not create a new premium-customer when parameters are empty")
    public void t2() throws Exception {
        // scenario
        NewCustomerRequest request = new NewCustomerRequest("", "");

        // action
        mockMvc.perform(post("/api/v1/premium-customers")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(2)))
                .andExpect(jsonPath("$.violations", containsInAnyOrder(
                                violation("name", "must not be empty"),
                                violation("email", "must not be empty")
                        )
                ))
        ;

        // validation
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("should not create a new premium-customer when parameters are invalid")
    public void t3() throws Exception {
        // scenario
        NewCustomerRequest request = new NewCustomerRequest(
                "a".repeat(61),
                "b".repeat(61)
        );

        // action
        mockMvc.perform(post("/api/v1/premium-customers")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(request))
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, is(MediaTypes.PROBLEM_VALUE)))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(3)))
                .andExpect(jsonPath("$.violations", containsInAnyOrder(
                                violation("name", "size must be between 0 and 60"),
                                violation("email", "size must be between 0 and 60"),
                                violation("email", "must be a well-formed email address")
                        )
                ))
        ;

        // validation
        assertEquals(0, repository.count());
    }

    private String toJson(Object payload) throws JsonProcessingException {
        return mapper.writeValueAsString(payload);
    }

    private Map<String, Object> violation(String field, String message) {
        return Map.of(
                "field", field,
                "message", message
        );
    }

}