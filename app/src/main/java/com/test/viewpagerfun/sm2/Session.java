package com.test.viewpagerfun.sm2;

import java.util.*;

public class Session {

    private List<Review> reviews = new ArrayList<>();

    public void applyReview(Review review, int quality) {
        review.setQuality(quality);

        if(review.getQuality() < 2){
            review.setFailedInSession(true);
        }

        reviews.add(review);
    }


    public List<Review> getReviews() {
        return reviews;
    }
}
