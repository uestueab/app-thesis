package com.thesis.yokatta.commander.state;

import android.app.Activity;

import com.thesis.yokatta.databinding.ReviewInputFragmentBinding;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MismatchToastState {

    private Activity activity;
    private ReviewInputFragmentBinding binding;
    private int distance;
    private String meaning;

}