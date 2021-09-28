package com.test.viewpagerfun;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.test.viewpagerfun.commander.Commander;
import com.test.viewpagerfun.commander.commands.ReviewAnimationCommand;
import com.test.viewpagerfun.commander.commands.ShuffleCardsCommand;
import com.test.viewpagerfun.commander.receiver.ReviewAnimation;
import com.test.viewpagerfun.commander.state.ReviewAnimationState;
import com.test.viewpagerfun.databinding.ReviewDetailedResultFragmentBinding;
import com.test.viewpagerfun.listeners.onClick.NextReviewItemListener;
import com.test.viewpagerfun.sm2.Review;
import com.test.viewpagerfun.viewmodel.SharedViewModel;
import static com.test.viewpagerfun.constants.ConstantsHolder.*;
public class ReviewDetailedResultFragment extends Fragment {

    //make communication between fragments possible
    private SharedViewModel model;
    //view binding of fragment
    private ReviewDetailedResultFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ReviewDetailedResultFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //Prepare all features associated with this particular fragment
        Commander.init();
        Commander.setCommand(PREFS_REVIEW_SHUFFLE, new ShuffleCardsCommand());
        Commander.setCommand(PREFS_DISPLAY_ANIMATION, new ReviewAnimationCommand());

        //Update the UI.
        Review review = model.getMostRecentReview();
        binding.tvQuestion.setText(review.getNote().getPrompt());
        binding.tvReviewProgress.setText(model.getCorrectCount(false)+"/"+model.getTotalNotes());
        if (review.hasFailed()) {
            binding.tvAnswerResult.setText("wrong");
            binding.tvAnswerResult.setBackgroundColor(
                    ContextCompat.getColor(getActivity(),R.color.wrong));

        } else {
            binding.tvAnswerResult.setText("correct");
            binding.tvAnswerResult.setBackgroundColor(
                    ContextCompat.getColor(getActivity(),R.color.correct)
            );
        }

        //FEATURE: Play review animation
        Commander.setState(PREFS_DISPLAY_ANIMATION,
                ReviewAnimationState.builder().binding(binding).hasFailed(review.hasFailed()).build());
        Commander.run(PREFS_DISPLAY_ANIMATION);

        /*  when a note fails during review add it on top of the list stack.
            This causes the review to be finished only if all items have passed correctly.
         */
        model.getNotes().observe(getViewLifecycleOwner(), notes -> {
            if(notes.size() > 0)
                notes.remove(0);

            if (review.hasFailed()){
                notes.add(review.getNote());
                //FEATURE: Shuffle based on preference
                Commander.setState(PREFS_REVIEW_SHUFFLE,notes);
                Commander.run(PREFS_REVIEW_SHUFFLE);
            }
        });

        //Decides finishing the review, or showing next item in queue.
        NextReviewItemListener nextReviewItemListener = NextReviewItemListener.builder()
                .activity(getActivity())
                .model(model)
                .binding(binding)
                .build();

        binding.btnNextTop.setOnClickListener(nextReviewItemListener);
        binding.btnNextBottom.setOnClickListener(nextReviewItemListener);

    }


    //Fragments outlive their views. clean up any references to the binding class instance in the fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

