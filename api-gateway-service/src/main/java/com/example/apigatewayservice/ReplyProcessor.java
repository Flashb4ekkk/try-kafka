package com.example.apigatewayservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ReplyProcessor {
    private final CompletableFuture<String> future = new CompletableFuture<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "auth-service-response-topic", groupId = "get_optional_user")
    public void processReply(String userJson) {
        System.out.println("Received reply: " + userJson);
        future.complete(userJson);
    }

    public CompletableFuture<Optional<User>> waitForReply() {
        return future.thenApply(userJson -> {
            try {
                User user = objectMapper.readValue(userJson, User.class);
                return Optional.of(user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }
}
