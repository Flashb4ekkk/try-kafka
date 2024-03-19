package com.example.wishlistservice;

import com.example.wishlistservice.dto.BookDTO;
import com.example.wishlistservice.dto.RequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping("/add-book")
    @Transactional
    public ResponseEntity<?> addBookToWishList(@RequestBody RequestDTO request) {
        return wishListService.addBookToWishList(request);
    }

    @GetMapping("/{email}")
    @Transactional
    public ResponseEntity<?> getWishList(@PathVariable String email) {
        List<BookDTO> books = wishListService.getWishListByEmail(email);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{email}")
    @Transactional
    public ResponseEntity<Void> clearWishList(@PathVariable String email) {
        wishListService.clearWishList(email);
        return ResponseEntity.noContent().build();
    }
}
