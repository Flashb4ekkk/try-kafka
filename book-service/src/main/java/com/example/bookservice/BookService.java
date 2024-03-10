package com.example.bookservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookDAO bookRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ReplyProcessor replyProcessor;

    public Book saveBook(BookDTO book, MultipartFile image, String email) throws IOException { // KAFKA
        kafkaTemplate.send("book-service-request-topic", email);
        CompletableFuture<Long> revieweeIdFuture = replyProcessor.waitForReply();
        Long userId = revieweeIdFuture.join();
        Book newBook = Book.builder()
                .title(book.getTitle())
                .image(image.getBytes())
                .description(book.getDescription())
                .author(book.getAuthor())
                .year(book.getYear())
                .publishedBy(book.getPublishedBy())
                .price(book.getPrice())
                .genres(book.getGenres())
                .userId(userId)
                .status(BookStatus.AVAILABLE)
                .build();
        return bookRepository.save(newBook);
    }

    public Book getBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new RuntimeException("Book not found");
        }
        return book;
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getAllBooksById(String email) { // KAFKA
        kafkaTemplate.send("get_user_id", email);
        CompletableFuture<Long> revieweeIdFuture = replyProcessor.waitForReply();
        Long revieweeId = revieweeIdFuture.join();
        return bookRepository.findByUserId(revieweeId);
    }

    public Book updateBook(Book book, Long id) {
        Book bookDetails = bookRepository.findById(id).orElse(null);
        if (bookDetails == null) {
            throw new RuntimeException("Book not found");
        }
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

    public List<Book> getAllAvailable() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .collect(Collectors.toList());
    }
}