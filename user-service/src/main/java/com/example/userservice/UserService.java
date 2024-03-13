package com.example.userservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDAO userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "book-service-request-topic", groupId = "book-service")
    public void handleGetUserIdFromBookService(String email) {
        Long userId = findIdByEmail(email);
        System.out.println(userId + " book service");
        kafkaTemplate.send("book-service-response-topic", Long.toString(userId));
    }

    @KafkaListener(topics = "review-service-request-topic", groupId = "review-service")
    public void handleGetUserIdFromReviewService(String email) {
        Long userId = findIdByEmail(email);
        System.out.println(userId + " review service");
        kafkaTemplate.send("review-service-response-topic", Long.toString(userId));
    }

    public Optional<User> findByEmail(String username) throws Exception {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new Exception("User not found"));
        if(user.getImage() == null) {
            String imagePath = "C:\\Users\\roman\\IdeaProjects\\micro-service-try-kafka\\user-service\\src\\main\\resources\\static\\default-avatar.png";
            try {
                byte[] defaultImage = Files.readAllBytes(Paths.get(imagePath));
                user.setImage(defaultImage);
            } catch (IOException e) {
                throw new Exception("Error reading default image", e);
            }
        }
        return Optional.of(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public User createUser(UserRegistration userRegistration) {
        User user = User.builder()
                .username(userRegistration.getUsername())
                .email(userRegistration.getEmail())
                .firstName(userRegistration.getFirstName())
                .lastName(userRegistration.getLastName())
                .mobilePhone(passwordEncoder.encode(userRegistration.getPassword()))
//                .mobilePhone(userRegistration.getPassword())
                .password(passwordEncoder.encode(userRegistration.getPassword()))
//                .password(userRegistration.getPassword())
                .buck(5L)
                .rating(7.0)
                .role(Role.ROLE_USER)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> findByEmailForCheck(String email) {
        return userRepository.findByEmail(email);
    }

    public Long findIdByEmail(String email) {
        return userRepository.findIdByEmail(email);
    }

    public void addProfileImage(MultipartFile file, String name) {
        User user = userRepository.findByEmail(name).get();
        try {
            user.setImage(file.getBytes());
            userRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBucksToUser(String name, Long bucks) {
        User user = userRepository.findByEmail(name).get();
        user.setBuck(user.getBuck() + bucks);
        userRepository.save(user);
    }
}