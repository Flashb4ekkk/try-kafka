package com.example.apigatewayservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apii")
public class Controller {


    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/add")
    public String add() {
        return "add";
    }
}
