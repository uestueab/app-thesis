package com.test.viewpagerfun.commander.state;

import android.app.Activity;
import android.widget.Toast;

import com.test.viewpagerfun.databinding.ReviewInputFragmentBinding;

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