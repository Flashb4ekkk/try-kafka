package com.example.bookservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ReplyProcessor {
    private final CompletableFuture<String> future = new CompletableFuture<>();

    @KafkaListener(topics = "book-service-response-topic", groupId = "get_user_id")
    public void processReply(String userId) {
        System.out.println("Received reply: " + userId);
        future.complete(userId);
    }

    public CompletableFuture<Long> waitForReply() {
        return future.thenApply(Long::parseLong);
    }
}