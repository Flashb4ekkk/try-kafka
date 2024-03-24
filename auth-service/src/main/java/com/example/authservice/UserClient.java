package com.example.authservice;

import com.example.authservice.dto.User;
import com.example.authservice.dto.UserRegistration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://user-service:8082")
//@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserClient {

    @GetMapping("/api/user/checkEmail/{email}")
    Optional<User> findByEmailForCheck(@PathVariable String email);

    @PostMapping("/api/user/register")
    User createUser(@RequestBody UserRegistration regRequest);

    @GetMapping("/api/user/refreshToken/{refreshToken}")
    User findByRefreshToken(@PathVariable String refreshToken);

    @PutMapping("/api/user/update")
    void updateUser(@RequestBody User user);
}

