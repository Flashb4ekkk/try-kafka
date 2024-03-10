package workplace.configserver;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic requestTopicReview() {
        return new NewTopic("review-service-request-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic responseTopicReview() {
        return new NewTopic("review-service-response-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic requestTopicBook() {
        return new NewTopic("book-service-request-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic responseTopicBook() {
        return new NewTopic("book-service-response-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic requestTopicAuth() {
        return new NewTopic("auth-service-request-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic responseTopicAuth() {
        return new NewTopic("auth-service-response-topic", 2, (short) 1);
    }
}

