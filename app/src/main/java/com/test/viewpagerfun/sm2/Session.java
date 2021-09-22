package com.test.viewpagerfun.sm2;

import com.test.viewpagerfun.model.entity.Note;

import java.util.*;


public class Session {
    private Map<Note, SessionNoteStatistics> noteStatisticsMap = new HashMap<>();

    public void applyReview(Review review) {
        Note note = review.getNote();
        SessionNoteStatistics noteStatistics = noteStatisticsMap.computeIfAbsent(note, k -> new SessionNoteStatistics());
        noteStatistics.setMostRecentScore(review.getScore());

        if (review.getScore() < 2) {
            noteStatistics.setLapsedDuringSession(true);
        }
    }

    public Map<Note, SessionNoteStatistics> getNoteStatistics() {
        return this.noteStatisticsMap;
    }
}
