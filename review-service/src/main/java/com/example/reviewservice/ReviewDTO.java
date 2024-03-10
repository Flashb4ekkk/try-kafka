package com.example.reviewservice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDTO {
    private String content;
    private double rating;
    private String revieweeEmail;
}