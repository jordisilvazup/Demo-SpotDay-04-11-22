package stack.edu.Demo.samples.aws.dynamodb.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    @Override
    public String convert(LocalDateTime source) {
        return source.format(ISO_LOCAL_DATE_TIME);
    }

    @Override
    public LocalDateTime unconvert(String source) {
        return LocalDateTime.parse(source, ISO_LOCAL_DATE_TIME);
    }

}
