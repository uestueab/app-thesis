package com.test.viewpagerfun.sm2;

import java.util.*;

public class Session {

    private final List<Review> reviews = new ArrayList<>();

    // determines if the review failed and wether it needs to be reviewed again.
    public void applyReview(Review review) {

        if(review.getQuality() < 2){
            review.setFailedInSession(true);
        }

        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
