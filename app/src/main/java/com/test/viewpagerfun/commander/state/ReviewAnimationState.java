package com.test.viewpagerfun.commander.state;

import com.test.viewpagerfun.databinding.ReviewDetailedResultFragmentBinding;

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