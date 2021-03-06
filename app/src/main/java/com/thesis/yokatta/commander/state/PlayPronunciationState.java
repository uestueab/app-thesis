package com.thesis.yokatta.commander.state;

import android.app.Activity;
import android.content.ContextWrapper;
import android.widget.Toast;

import com.thesis.yokatta.model.entity.FlashCard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PlayPronunciationState {
    private ContextWrapper contextWrapper;
    private FlashCard flashCard;
}