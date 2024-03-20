package com.example.userservice.kafka;

import com.example.userservice.User;
import com.example.userservice.UserService;
import com.example.userservice.dto.UserReviewDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Review
    @KafkaListener(topics = "user-service-request-get-user-by-email-topic", groupId = "user-service")
    @Transactional
    public void handleGetUserFromReviewService(@Payload String email, @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic) throws Exception {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));
        UserReviewDTO userReviewDTO = UserReviewDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .rating(user.getRating())
                .image(user.getImage())
                .buck(user.getBuck())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Sending user: " + replyTopic);
        try {
            String userJson = mapper.writeValueAsString(userReviewDTO);
            kafkaTemplate.send(replyTopic, userJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing user", e);
        }
    }


    @KafkaListener(topics = "user-service-request-send-rating-topic", groupId = "user-service")
    @Transactional
    public void handleGetRatingFromReviewService(String request) {
        String[] parts = request.split(":");
        String email = parts[0];
        double rating = Double.parseDouble(parts[1]);
        int reviewsCount = Integer.parseInt(parts[2]);
        User user = userService.findByEmailForCheck(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        double newRating;
        if(reviewsCount == 0) {
            newRating = (user.getRating() + rating) / 2.;
        } else {
            newRating = (user.getRating() * reviewsCount + rating) / (reviewsCount + 1);
        }
        newRating = Math.round(newRating * 10.0) / 10.0; // Round to one decimal place
        user.setRating(newRating);
        userService.updateUser(user);
    }

    @KafkaListener(topics = "user-service-request-update-user-topic", groupId = "user-service")
    public void handleUpdateUserRequest(String request) {
        String[] parts = request.split(":");
        String email = parts[0];
        long bucks = Long.parseLong(parts[1]);
        User user = userService.findByEmailForCheck(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBuck(bucks);
        userService.updateUser(user);
    }
}
