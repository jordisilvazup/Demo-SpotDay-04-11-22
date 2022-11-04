package stack.edu.Demo.samples.aws.dynamodb;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DynamoDbConfig.DynamoDbProperties.class)
@EnableDynamoDBRepositories(basePackageClasses = DynamoDbConfig.class)
public class DynamoDbConfig {

    @Autowired
    private DynamoDbProperties dynamoDbProperties;

    /**
     * Creates an instance of DynamoDB client
     */
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {

        AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(dynamoDbProperties.accessKey(), dynamoDbProperties.secretKey())
        );

        EndpointConfiguration endpointConfig
                = new EndpointConfiguration(dynamoDbProperties.endpoint(), dynamoDbProperties.region());

        AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder
                                            .standard()
                                            .disableEndpointDiscovery()
                                            .withCredentials(credentials)
                                            .withEndpointConfiguration(endpointConfig)
                                            .build();

        return dynamoDbClient;
    }

    /**
     * Properties related to DynamoDB integration
     */
    @ConfigurationProperties("amazon.aws.dynamodb")
    static record DynamoDbProperties(String accessKey, String secretKey, String endpoint, String region) {
    }

}
