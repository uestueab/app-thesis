package com.thesis.yokatta.sm2;

import com.thesis.yokatta.model.entity.FlashCard;

import java.util.*;


public class Session {
    private Map<FlashCard, SessionFlashCardStatistics> flashCardStatisticsMap = new HashMap<>();

    public void applyReview(Review review) {
        FlashCard flashCard = review.getFlashCard();
        SessionFlashCardStatistics flashCardStatistics = flashCardStatisticsMap.computeIfAbsent(flashCard, k -> new SessionFlashCardStatistics());
        flashCardStatistics.setMostRecentScore(review.getScore());

        if (review.hasFailed()) {
            flashCardStatistics.setFailedDuringSession(true);
        }
    }

    public Map<FlashCard, SessionFlashCardStatistics> getFlashCardStatistics() {
        return this.flashCardStatisticsMap;
    }
}
