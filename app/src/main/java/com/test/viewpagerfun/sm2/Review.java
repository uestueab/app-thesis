package com.test.viewpagerfun.sm2;

import com.test.viewpagerfun.model.entity.Note;

import lombok.Getter;
import lombok.Setter;

/*
 *  Review consists of the note and the algorithm related parameters.
 *  quality: is 0 on instantiation and changes latter in the applyReview method in the session.
 *  failedInSession: is 'false' on instantiation and changes latter in the applyReview method in the session.
 */
@Getter
@Setter
public class Review {
    private Note note;

    private int quality;
    private boolean failedInSession = false;

    /* quality of the current review item:
     *
     * 3 - perfect answer
     * 2 - hesitant
     * 1 - incorrect
     * 0 - never reviewed
     */

    public Review(Note note, int quality) {
        this.note = note;
        this.quality = quality;
    }
}
