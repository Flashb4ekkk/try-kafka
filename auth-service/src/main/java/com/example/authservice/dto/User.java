package com.example.authservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobilePhone;
    private Double rating;
    private byte[] image;
    private Long buck;
    private Role role;
    private List<Long> wishListBookIds;
    private String refreshToken;
}