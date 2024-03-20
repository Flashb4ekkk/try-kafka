package com.example.userservice;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<User> findByEmail(String username) throws Exception {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new Exception("User not found"));
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
                .password(passwordEncoder.encode(userRegistration.getPassword()))
                .buck(5L)
                .rating(3.5)
                .role(Role.ROLE_USER)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> findByEmailForCheck(String email) {
        return userRepository.findByEmail(email);
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