package com.example.reviewservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ReviewService {

    @Autowired
    private ReviewDAO reviewRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ReplyProcessor replyProcessor;

    public List<Review> getReviewsByUserEmail(String email) {
        kafkaTemplate.send("review-service-request-topic", email);
        CompletableFuture<Long> revieweeIdFuture = replyProcessor.waitForReply();
        Long revieweeId = revieweeIdFuture.join();
        return reviewRepository.findAllByRevieweeId(revieweeId);
    }



    public Review createReview(ReviewDTO review, String email) {
        kafkaTemplate.send("review-service-request-topic", email);
        CompletableFuture<Long> reviewerIdFuture = replyProcessor.waitForReply();
        kafkaTemplate.send("review-service-request-topic", review.getRevieweeEmail());
        CompletableFuture<Long> revieweeIdFuture = replyProcessor.waitForReply();

        Long reviewerId = reviewerIdFuture.join();
        Long revieweeId = revieweeIdFuture.join();

        if (reviewerId != null && revieweeId != null) {
            if (review.getRating() < 1 || review.getRating() > 10) {
                throw new IllegalArgumentException("Rating must be between 1 and 10");
            }

            Review newReview = Review.builder()
                    .content(review.getContent())
                    .rating(review.getRating())
                    .reviewerId(reviewerId)
                    .revieweeId(revieweeId)
                    .build();

            return reviewRepository.save(newReview);
        } else {
            throw new IllegalArgumentException("Reviewer or reviewee not found");
        }
    }


    public void deleteById(Long id) {
        if(!reviewRepository.existsById(id)){
            throw new IllegalArgumentException("Review with id " + id + " not found");
        }
        reviewRepository.deleteById(id);
    }
}