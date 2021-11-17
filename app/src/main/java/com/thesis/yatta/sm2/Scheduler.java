package com.thesis.yatta.sm2;

import android.util.Log;

import com.thesis.yatta.model.entity.FlashCard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Builder
public class Scheduler {

    private final float MIN_EASINESS_FACTOR = 1.3f;
    private final float MAX_EASINESS_FACTOR = 2.5f;
    private final int HOURS_PER_DAY = 24;

    private final Map<Integer, Float> defaultConsecutiveCorrectIntervalMappings =
            new HashMap<Integer, Float>() {{
                put(1, 1f);
                put(2, 6f);
            }};



    @Getter
    @Setter
    @Builder.Default
    private Map<Integer, Float> consecutiveCorrectIntervalMappings = new HashMap<>();

    @Builder.Default
    private Set<FlashCard> flashCards = new HashSet<>();

    public void addFlashCard(FlashCard flashCard) {
        flashCards.add(flashCard);
    }

    public Set<FlashCard> getFlashCards() {
        return flashCards;
    }

    public void applySession(Session session) {
        session.getFlashCardStatistics().forEach((flashCard, statistics) -> {
            updateFlashCardInterval(flashCard, statistics);
            updateFlashCardSchedule(flashCard);
        });
    }

    protected void updateFlashCardInterval(FlashCard flashCard, SessionFlashCardStatistics statistics) {

        if (statistics.isFailedDuringSession() && statistics.getMostRecentScore() > 1) {

            // flashCard lapsed but the most recent review was successful.
            // reset interval and correct count without updating the flashCard's easiness factor
            flashCard.setConsecutiveCorrectCount(1);
            flashCard.setInterval(getConsecutiveCorrectInterval(1));
        } else if (statistics.getMostRecentScore() < 2 ) {

            // last review for this flashCard was not successful. set interval and consecutive correct count to 0
            flashCard.setInterval(0);
            flashCard.setConsecutiveCorrectCount(0);
        } else {
            // flashCard was recalled successfully during this session without a lapse; increment the correct count
            flashCard.setConsecutiveCorrectCount(flashCard.getConsecutiveCorrectCount() + 1);

            // review was successful. update flashCard easiness factor then calculate new interval
            float newEasinessFactor = calculateEasinessFactor(flashCard,statistics);
            flashCard.setEasinessFactor(newEasinessFactor);
            // either update interval based on a static mapping, or based on the previous interval * EF.
            // default static mappings are based on SM2 defaults (1 day then 6 days) but this can be overridden.
            Float fixedInterval = getConsecutiveCorrectInterval(flashCard.getConsecutiveCorrectCount());

            Float interval = Optional.ofNullable(fixedInterval)
                    .orElse((float)Math.round(flashCard.getInterval() * flashCard.getEasinessFactor()));
            flashCard.setInterval(interval);
        }
    }

    protected float calculateEasinessFactor(FlashCard flashCard, SessionFlashCardStatistics statistics){
        float newEasinessFactor = Math.max(MIN_EASINESS_FACTOR, (float)(flashCard.getEasinessFactor()
                + (0.1 - (3 - statistics.getMostRecentScore()) * (0.08 + (3 - statistics.getMostRecentScore()) * 0.02))));

//        newEasinessFactor = Math.min(newEasinessFactor,MAX_EASINESS_FACTOR);

        if(newEasinessFactor > MAX_EASINESS_FACTOR)
            newEasinessFactor = MAX_EASINESS_FACTOR;

        return newEasinessFactor;

    }

    protected void updateFlashCardSchedule(FlashCard flashCard) {
        int intervalDaysWhole = (int)flashCard.getInterval();
        float intervalDaysFraction = flashCard.getInterval() - intervalDaysWhole;
        Log.d("[Interval]::::::", String.valueOf(intervalDaysWhole));

//        LocalDateTime lastReview = TimeProvider.toLocalDateTime(flashCard.getLastReviewedDate());
//        lastReview.plusDays(intervalDaysWhole)
//                .plusHours(Math.round(HOURS_PER_DAY * intervalDaysFraction));
//        Log.d("[lastReview]::::::", lastReview.toString());

        flashCard.setLastReviewedDate(
               flashCard.getLastReviewedDate() + TimeUnit.DAYS.toMillis(intervalDaysWhole)
                + TimeUnit.HOURS.toMillis(Math.round(HOURS_PER_DAY * intervalDaysFraction))
        );

        flashCard.setDueDate(flashCard.getLastReviewedDate());
    }

    protected Float getConsecutiveCorrectInterval(int consecutiveCorrect) {
        return consecutiveCorrectIntervalMappings.getOrDefault(consecutiveCorrect,
                defaultConsecutiveCorrectIntervalMappings.get(consecutiveCorrect));
    }
}
