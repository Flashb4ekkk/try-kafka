//package com.example.apigatewayservice;
//
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@FeignClient(name = "user-service", url = "http://localhost:8082")
//public interface UserClient {
//
//    @GetMapping("/api/user/checkEmail/{email}")
//    Optional<User> findByEmailForCheck(@PathVariable String email);
//
//}
