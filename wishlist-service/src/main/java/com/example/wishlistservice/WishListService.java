package com.example.wishlistservice;

import com.example.wishlistservice.dto.BookDTO;
import com.example.wishlistservice.dto.RequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ReplyProcessor replyProcessor;

    public ResponseEntity<?> addBookToWishList(RequestDTO request) {
        WishList wishList = wishListRepository.findByUserEmail(request.getEmail())
                .orElseGet(() -> {
                    WishList newWishList = new WishList();
                    newWishList.setUserEmail(request.getEmail());
                    newWishList.setBooksId(new ArrayList<>());
                    return newWishList;
                });

        CompletableFuture<String> bookExistFuture = replyProcessor.waitForReply();
        kafkaTemplate.send(MessageBuilder.withPayload(request.getBookId().toString())
                .setHeader(KafkaHeaders.TOPIC, "book-service-request-check-exist-book-topic")
                .setHeader(KafkaHeaders.REPLY_TOPIC, "wishlist-service-response-check-topic")
                .setHeader("serviceName", "wishlist-service")
                .build());
        String bookExists;
        try {
            bookExists = bookExistFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if ("true".equalsIgnoreCase(bookExists)) {
            if (!wishList.getBooksId().contains(request.getBookId())) {
                wishList.getBooksId().add(request.getBookId());
                return ResponseEntity.ok(wishListRepository.save(wishList));
            }
            else {
                return ResponseEntity.badRequest().body("Book already exists in wishlist");
            }
        } else {
            return ResponseEntity.badRequest().body("Book not found");
        }
    }

    public List<BookDTO> getWishListByEmail(String email) {
        WishList wishList = wishListRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    WishList newWishList = new WishList();
                    newWishList.setUserEmail(email);
                    newWishList.setBooksId(new ArrayList<>());
                    return newWishList;
                });
        List<BookDTO> books = new ArrayList<>();

        for (Long bookId : wishList.getBooksId()) {
            CompletableFuture<String> bookFuture = replyProcessor.waitForReply();
            kafkaTemplate.send(MessageBuilder.withPayload(bookId.toString())
                    .setHeader(KafkaHeaders.TOPIC, "book-service-request-get-book-by-id-topic")
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "wishlist-service-response-get-book-by-id-topic")
                    .setHeader("serviceName", "wishlist-service")
                    .build());
            String bookJson = bookFuture.join();
            ObjectMapper mapper = new ObjectMapper();
            try {
                BookDTO book = mapper.readValue(bookJson, BookDTO.class);
                books.add(book);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing book", e);
            }
        }
        return books;
    }

    public void clearWishList(String email) {
        List<WishList> wishLists = wishListRepository.findAllByUserEmail(email);
        if (!wishLists.isEmpty()) {
            for (WishList wishList : wishLists) {
                wishList.getBooksId().clear();
                wishListRepository.save(wishList);
            }
        } else {
            throw new ResourceNotFoundException("Wishlist not found with email : " + email);
        }
    }
}
