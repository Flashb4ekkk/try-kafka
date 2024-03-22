package com.example.apigatewayservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/apii/test")
    public String test() {
        return "Api Gateway Service";
    }

    @GetMapping("/apii/add")
    public String test2() {
        return "Api Gateway Service 2";
    }
}
