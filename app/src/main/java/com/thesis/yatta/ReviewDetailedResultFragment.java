package com.thesis.yatta;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.thesis.yatta.commander.Commander;
import com.thesis.yatta.commander.commands.ReviewAnimationCommand;
import com.thesis.yatta.commander.commands.ShuffleCardsCommand;
import com.thesis.yatta.commander.state.ReviewAnimationState;
import com.thesis.yatta.databinding.ReviewDetailedResultFragmentBinding;
import com.thesis.yatta.listeners.onClick.NextReviewItemListener;
import com.thesis.yatta.sm2.Review;
import com.thesis.yatta.viewmodel.SharedViewModel;

import java.io.IOException;

import static com.thesis.yatta.constants.ConstantsHolder.*;
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
        binding.tvQuestion.setText(review.getFlashCard().getPrompt());
        binding.tvReviewProgress.setText(model.getCorrectCount(true)+"/"+model.getTotalFlashCards());
        if (review.hasFailed()) {
            binding.tvAnswerResult.setText("wrong");
            binding.tvAnswerResult.setBackgroundColor(
                    ContextCompat.getColor(getActivity(),R.color.wrong));

        } else {
            binding.tvAnswerResult.setText("correct");
            binding.tvAnswerResult.setBackgroundColor(
                    ContextCompat.getColor(getActivity(),R.color.correct)
            );

            String pronunciation = review.getFlashCard().getPronunciation();
            playPronunciation(pronunciation);
        }

        //FEATURE: Play review animation
        Commander.setState(PREFS_DISPLAY_ANIMATION,
                ReviewAnimationState.builder().binding(binding).hasFailed(review.hasFailed()).build());
        Commander.run(PREFS_DISPLAY_ANIMATION);


        /*  when a flashCard fails during review add it on top of the list stack.
            This causes the review to be finished only if all items have passed correctly.
         */
        model.getFlashCards().observe(getViewLifecycleOwner(), flashCards -> {
            if(flashCards.size() > 0)
                flashCards.remove(0);

            if (review.hasFailed()){
                flashCards.add(review.getFlashCard());
                //FEATURE: Shuffle based on preference
                Commander.setState(PREFS_REVIEW_SHUFFLE,flashCards);
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

    private void playPronunciation(String pronunciation){
        if(pronunciation != null){
            MediaPlayer mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(pronunciation);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    //Fragments outlive their views. clean up any references to the binding class instance in the fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

