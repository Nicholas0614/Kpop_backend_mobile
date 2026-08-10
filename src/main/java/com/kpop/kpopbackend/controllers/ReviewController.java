package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Review;
import com.kpop.kpopbackend.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping
    public ResponseEntity<Object> addReview( @RequestBody Review review ) {
        return reviewService.addReview(review);
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getReviews(@PathVariable int productId) {
        return reviewService.getReviewsByProduct(productId);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable int id) {
        return reviewService.deleteReview(id);
    }

}