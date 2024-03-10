package com.example.bookservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add/{email}")
    @Transactional
//    public Book addBook(@RequestParam("book") String bookStr, @RequestParam("image") MultipartFile image, Principal principal) throws IOException {
    public Book addBook(@RequestParam("book") String bookStr, @RequestParam("image") MultipartFile image, @PathVariable String email) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BookDTO bookDTO = objectMapper.readValue(bookStr, BookDTO.class);
        return bookService.saveBook(bookDTO, image, email);
    }

    @PutMapping("/edit/{id}")
    public Book updateBook(@PathVariable Long id, @RequestParam("book") String bookStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Book bookDetails = objectMapper.readValue(bookStr, Book.class);
        return bookService.updateBook(bookDetails, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if(bookService.getBook(id) == null) {
            return ResponseEntity.badRequest().body("Book not found");
        }
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book removed ! " + id);
    }

    @GetMapping("/get-email/{email}")
    @Transactional
    public List<Book> getBooksByEmail(@PathVariable String email) {
        return bookService.getAllBooksById(email);
    }


    @GetMapping("/get-all")
    @Transactional
    public List<Book> getAllBooks() {
        return bookService.getAllAvailable();
    }
}
