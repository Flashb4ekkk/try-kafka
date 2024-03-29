package com.example.bookservice;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.dto.BookUserDTO;
import com.example.bookservice.dto.UserDTO;
import com.example.bookservice.kafka.ReplyProcessor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookDAO bookRepository;

    @Autowired
    private ReplyProcessor replyProcessor;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Book saveBook(BookDTO book, MultipartFile image, String email) throws IOException {
        Book newBook = Book.builder()
                .id(book.getId())
                .title(book.getTitle())
                .image(image.getBytes())
                .description(book.getDescription())
                .author(book.getAuthor())
                .year(book.getYear())
                .publishedBy(book.getPublishedBy())
                .price(book.getPrice())
                .genres(book.getGenres())
                .userEmail(email)
                .status(BookStatus.AVAILABLE)
                .build();
        return bookRepository.save(newBook);
    }


    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<BookUserDTO> getAllBooksByEmail(String email) {
        List<Book> books = bookRepository.findByUserEmail(email);
        if (books.isEmpty()) {
            return List.of();
        }

        CompletableFuture<String> userFuture = replyProcessor.waitForReply();
        kafkaTemplate.send(MessageBuilder.withPayload(email)
                .setHeader(KafkaHeaders.TOPIC, "user-service-request-get-user-by-email-topic")
                .setHeader(KafkaHeaders.REPLY_TOPIC, "book-service-response-get-user-by-email-topic")
                .setHeader("serviceName", "book-service")
                .build());
        String userJson = userFuture.join();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        UserDTO userDTO;
        System.out.println("userJson: ");
        try {
            userDTO = mapper.readValue(userJson, UserDTO.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing user", e);
        }

        return books.stream().map(book -> BookUserDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .image(book.getImage())
                .genres(book.getGenres())
                .description(book.getDescription())
                .author(book.getAuthor())
                .year(book.getYear())
                .publishedBy(book.getPublishedBy())
                .price(book.getPrice())
                .user(userDTO)
                .build()).collect(Collectors.toList());
    }

    //    public List<Book> getAllAvailable() {
//        List<Book> books = bookRepository.findAll();
//        return books.stream()
//                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
//                .collect(Collectors.toList());
//    }
    public List<BookUserDTO> getAllAvailable() {
        List<Book> books = bookRepository.findAll();
        books = books.stream()
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .toList();

        if (books.isEmpty()) {
            return List.of();
        }

        List<BookUserDTO> bookUserDTOs = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (Book book : books) {
            CompletableFuture<String> userFuture = replyProcessor.waitForReply();
            kafkaTemplate.send(MessageBuilder.withPayload(book.getUserEmail())
                    .setHeader(KafkaHeaders.TOPIC, "user-service-request-get-user-by-email-topic")
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "book-service-response-get-user-by-email-topic")
                    .setHeader("serviceName", "book-service")
                    .build());
            String userJson = userFuture.join();
            UserDTO userDTO;
            try {
                userDTO = mapper.readValue(userJson, UserDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing user", e);
            }

            bookUserDTOs.add(BookUserDTO.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .image(book.getImage())
                    .genres(book.getGenres())
                    .description(book.getDescription())
                    .author(book.getAuthor())
                    .year(book.getYear())
                    .publishedBy(book.getPublishedBy())
                    .price(book.getPrice())
                    .user(userDTO)
                    .build());
        }

        return bookUserDTOs;
    }


    public Book updateBook(Book book, Long id) {
        Book bookDetails = bookRepository.findById(id).orElse(null);
        if (bookDetails == null) {
            throw new RuntimeException("Book not found");
        }
        bookDetails.setId(book.getId());
        bookDetails.setTitle(book.getTitle());
        bookDetails.setImage(book.getImage());
        bookDetails.setDescription(book.getDescription());
        bookDetails.setAuthor(book.getAuthor());
        bookDetails.setYear(book.getYear());
        bookDetails.setPublishedBy(book.getPublishedBy());
        bookDetails.setPrice(book.getPrice());
        bookDetails.setGenres(book.getGenres());
        bookRepository.save(bookDetails);
        return bookDetails;
    }

    public void save(Book book) {
        bookRepository.save(book);
    }
}