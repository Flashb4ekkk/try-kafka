//package workplace.configserver;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConfiguration {
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "52428800"); // 50MB
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    // User
//    @Bean
//    public NewTopic requestGetUserTopic() {
//        return new NewTopic("user-service-request-get-user-by-email-topic", 2, (short) 1);
//    }
//
//    @Bean
//    public NewTopic requestGetUserRatingTopic() {
//        return new NewTopic("user-service-request-send-rating-topic", 2, (short) 1);
//    }
//
//    // Book
//    @Bean
//    public NewTopic requestCheckTopic() {
//        return new NewTopic("book-service-request-check-exist-book-topic", 2, (short) 1);
//    }
//
//    @Bean
//    public NewTopic requestGetBookTopic() {
//        return new NewTopic("book-service-request-get-book-by-id-topic", 2, (short) 1);
//    }
//
//    // Wishlist response
//    @Bean
//    public NewTopic responseCheckTopic() {
//        return new NewTopic("wishlist-service-response-check-topic", 2, (short) 1);
//    }
//
//    @Bean
//    public NewTopic responseGetBookTopic() {
//        return new NewTopic("wishlist-service-response-get-book-by-id-topic", 2, (short) 1);
//    }
//
//    // Review response
//    @Bean
//    public NewTopic responseGetUserTopic() {
//        return new NewTopic("review-service-response-get-user-by-email-topic", 2, (short) 1);
//    }
//}