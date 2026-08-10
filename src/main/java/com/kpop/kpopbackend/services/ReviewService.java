package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.dto.ReviewResponse;
import com.kpop.kpopbackend.models.Review;
import com.kpop.kpopbackend.models.User;
import com.kpop.kpopbackend.repository.ReviewRepository;
import com.kpop.kpopbackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }


    // Add Review
    public ResponseEntity<Object> addReview(Review review) {

        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }

        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment cannot be empty");
        }

        return ResponseEntity.ok(reviewRepository.save(review));
    }


    // Get Reviews By Product
    public ResponseEntity<Object> getReviewsByProduct(int productId) {

        List<Review> reviews = reviewRepository.findByProductId(productId);

        List<ReviewResponse> response = reviews.stream().map(review -> {

            User user = userRepository
                    .findById(review.getUserId())
                    .orElse(null);

            ReviewResponse dto = new ReviewResponse();

            dto.setId(review.getId());
            dto.setUserId(review.getUserId());
            dto.setProductId(review.getProductId());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setDate(review.getDate());

            if (user != null) {
                dto.setUserName(user.getName());
            }

            return dto;

        }).toList();

        return ResponseEntity.ok(response);
    }


    // Delete Review
    public ResponseEntity<Object> deleteReview(int id) {

        Review review = reviewRepository.findById(id).orElse(null);

        if (review == null) {
            return ResponseEntity.badRequest().body("Review not found");
        }

        reviewRepository.delete(review);

        return ResponseEntity.ok("Review deleted");
    }

}