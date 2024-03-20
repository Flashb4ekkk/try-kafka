package com.example.wishlistservice.dto;

import lombok.Data;

@Data
public class RequestDTO {
    private String email;
    private Long bookId;
}
