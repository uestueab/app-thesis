package com.test.viewpagerfun.sm2;

import com.test.viewpagerfun.model.entity.Note;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    public void applySession(Session session) {
        session.getReviews().forEach((review) -> {
            updateNoteInterval(review);
            updateNoteSchedule(review);
        });
    }

    protected void updateNoteInterval(Review review) {
        Note note = review.getNote();

        if (review.isFailedInSession() && review.getQuality() > 1) {

            // note lapsed but the most recent review was successful.
            // reset interval and correct count without updating the note's easiness factor
            note.setConsecutiveCorrectCount(1);
            note.setInterval(getConsecutiveCorrectInterval(1));
        } else if (review.getQuality() < 2) {

            // last review for this note was not successful. set interval and consecutive correct count to 0
            note.setInterval(0);
            note.setConsecutiveCorrectCount(0);
        } else {
            // note was recalled successfully during this session without a lapse; increment the correct count
            note.setConsecutiveCorrectCount(note.getConsecutiveCorrectCount() + 1);

            // review was successful. update note easiness factor then calculate new interval
            float newEasinessFactor = calculateEasinessFactor(review);
            note.setEasinessFactor(newEasinessFactor);
            // either update interval based on a static mapping, or based on the previous interval * EF.
            // default static mappings are based on SM2 defaults (1 day then 6 days) but this can be overridden.
            Float fixedInterval = getConsecutiveCorrectInterval(note.getConsecutiveCorrectCount());

            Float interval = Optional.ofNullable(fixedInterval)
                    .orElse((float) Math.round(note.getInterval() * note.getEasinessFactor()));
            note.setInterval(interval);
        }
    }

    protected float calculateEasinessFactor(Review review) {
        float newEasinessFactor = Math.max(MIN_EASINESS_FACTOR, (float) (review.getNote().getEasinessFactor()
                + (0.1 - (3 - review.getQuality()) * (0.08 + (3 - review.getQuality()) * 0.02))));

        if (newEasinessFactor > MAX_EASINESS_FACTOR)
            newEasinessFactor = MAX_EASINESS_FACTOR;

        return newEasinessFactor;

    }

    protected void updateNoteSchedule(Review review) {
        Note note = review.getNote();

        int intervalDaysWhole = (int)note.getInterval();
        float intervalDaysFraction = note.getInterval() - intervalDaysWhole;

        note.setLastReviewedDate(
                note.getLastReviewedDate() + TimeUnit.DAYS.toMillis(intervalDaysWhole)
                        + TimeUnit.HOURS.toMillis(Math.round(HOURS_PER_DAY * intervalDaysFraction))
        );

        note.setDueDate(note.getLastReviewedDate());
    }

    protected Float getConsecutiveCorrectInterval(int consecutiveCorrect) {
        return consecutiveCorrectIntervalMappings.getOrDefault(consecutiveCorrect,
                defaultConsecutiveCorrectIntervalMappings.get(consecutiveCorrect));
    }
}
