package com.example.finalproject.Classes;

import com.example.finalproject.Classes.User.User;

public class Review {
    private String reviewId;
    private float reviewAppScore;
    private String reviewText;
    private User reviewReviewer;

    public Review() {
    }

    public Review(float reviewAppScore, String reviewText, User reviewReviewer) {
        this.reviewAppScore = reviewAppScore;
        this.reviewText = reviewText;
        this.reviewReviewer = reviewReviewer;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public float getReviewAppScore() {
        return reviewAppScore;
    }

    public void setReviewAppScore(float reviewAppScore) {
        this.reviewAppScore = reviewAppScore;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public User getReviewReviewer() {
        return reviewReviewer;
    }

    public void setReviewReviewer(User reviewReviewer) {
        this.reviewReviewer = reviewReviewer;
    }
}
