package com.example.wishlistservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class ReplyProcessor {
    private CompletableFuture<String> future;

    @KafkaListener(topics = "wishlist-service-response-check-topic", groupId = "wishlist-service")
    public void processExistResponse(String reply) {
        if (!"true".equalsIgnoreCase(reply) && !"false".equalsIgnoreCase(reply)) {
            throw new RuntimeException("Invalid response: " + reply);
        }
        if (future != null) {
            future.complete(reply);
        }
    }

    @KafkaListener(topics = "wishlist-service-response-get-book-by-id-topic", groupId = "wishlist-service")
    public void processBookResponse(String bookJson) {
        if (future != null) {
            future.complete(bookJson);
        }
    }

    public CompletableFuture<String> waitForReply() {
        future = new CompletableFuture<>();
        return future;
    }
}