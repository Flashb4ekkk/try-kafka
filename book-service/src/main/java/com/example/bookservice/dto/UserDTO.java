package com.example.bookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Double rating;
    private byte[] image;
    private Long buck;
}
