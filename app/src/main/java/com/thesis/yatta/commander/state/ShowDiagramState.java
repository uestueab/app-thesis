package com.thesis.yatta.commander.state;

import android.content.Context;
import android.content.ContextWrapper;

import com.thesis.yatta.databinding.ActivityStartingScreenBinding;
import com.thesis.yatta.model.entity.FlashCard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ShowDiagramState {
    private ActivityStartingScreenBinding binding;
    private Context context;
}