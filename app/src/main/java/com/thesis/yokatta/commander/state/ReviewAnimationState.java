package com.thesis.yokatta.commander.state;

import com.thesis.yokatta.databinding.ReviewDetailedResultFragmentBinding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAnimationState {

    private ReviewDetailedResultFragmentBinding binding;
    private boolean hasFailed;

}