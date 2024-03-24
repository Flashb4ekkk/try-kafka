package com.example.apigatewayservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@org.springframework.stereotype.Controller


@RestController
public class Controller {

    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping("/tt")
    public String home() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("qwerty2@gmail.com", "qwertyP", new ArrayList<>());
        return jwtTokenService.generateJwt(userDetails);
    }
}
