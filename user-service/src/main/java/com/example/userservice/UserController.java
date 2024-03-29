package com.example.userservice;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/checkEmail/{email}")
    @Transactional
    public Optional<User> findByEmailForCheck(@PathVariable String email) {
        return userService.findByEmailForCheck(email);
    }

    @GetMapping("/refreshToken/{refreshToken}")
    public User findByRefreshToken(@PathVariable String refreshToken) {
        return userService.findByRefreshToken(refreshToken);
    }

    @PutMapping("/")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistration userRegistration) {
        return ResponseEntity.ok(userService.createUser(userRegistration));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok("User updated");
    }

    @PostMapping("/add-profile-image")
    @Transactional
    public ResponseEntity<?> addProfileImage(@RequestParam MultipartFile file, Principal principal) {
        userService.addProfileImage(file, principal.getName());
        return ResponseEntity.ok("Image added");
    }

    @GetMapping("/get-user/{email}")
    @Transactional
    public ResponseEntity<?> getUser(@PathVariable String email) throws Exception {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) throws Exception {
        if(userService.findByEmail(email).isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        userService.deleteUser(email);
        return ResponseEntity.ok("User deleted");
    }

    @PostMapping("/add-buck/{email}/{bucks}")
    @Transactional
    public ResponseEntity<?> addBucksToUser(@PathVariable String email, @PathVariable Long bucks) {
        userService.addBucksToUser(email, bucks);
        return ResponseEntity.ok("bucks added");
    }
}