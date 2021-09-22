package com.test.viewpagerfun.sm2;

import com.test.viewpagerfun.model.entity.Note;

import lombok.Getter;

@Getter
public class Review {
    private Note note;
    private int score;

    public Review(Note note, int score) {
        this.note = note;
        this.score = score;
    }
}
