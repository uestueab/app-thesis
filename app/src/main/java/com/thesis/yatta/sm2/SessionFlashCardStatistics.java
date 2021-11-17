package com.thesis.yatta.sm2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionFlashCardStatistics {
    private boolean failedDuringSession = false;

    /* mostRecentScore referes to the quality of the current review item:
     *
     * 3 - perfect answer
     * 2 - hesitant
     * 1 - incorrect
     * 0 - never reviewed
     */

    private int mostRecentScore = 0;
}
