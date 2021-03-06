package com.thesis.yokatta;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.thesis.yokatta.commander.Commander;
import com.thesis.yokatta.commander.commands.PlayPronunciationCommand;
import com.thesis.yokatta.commander.commands.ReviewAnimationCommand;
import com.thesis.yokatta.commander.commands.ShuffleCardsCommand;
import com.thesis.yokatta.commander.state.PlayPronunciationState;
import com.thesis.yokatta.commander.state.ReviewAnimationState;
import com.thesis.yokatta.databinding.ReviewDetailedResultFragmentBinding;
import com.thesis.yokatta.listeners.onClick.NextReviewItemListener;
import com.thesis.yokatta.sm2.Review;
import com.thesis.yokatta.viewmodel.SharedViewModel;

import java.util.List;

import static com.thesis.yokatta.constants.ConstantsHolder.*;
public class ReviewDetailedResultFragment extends Fragment {

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

        //make communication between fragments possible
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //Prepare all features associated with this particular fragment
        Commander.init();
        Commander.setCommand(PREFS_REVIEW_SHUFFLE, new ShuffleCardsCommand());
        Commander.setCommand(PREFS_DISPLAY_ANIMATION, new ReviewAnimationCommand());
        Commander.setCommand(PREFS_PLAY_PRONUNCIATION, new PlayPronunciationCommand());

        //Update the UI.
        Review review = model.getMostRecentReview();
        binding.tvQuestion.setText(review.getFlashCard().getPrompt());
        binding.tvReviewProgress.setText(model.getCorrectCount()+"/"+ model.getTotalFlashCards());
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

        String synonyms = prepareSynonyms(review);
        binding.tvMySynonym.setText(synonyms);
        binding.tvMnemonic.setText(review.getFlashCard().getMnemonic());
        //show consecutive correct count as streak
        binding.tvStreakValue.setText(String.valueOf(review.getFlashCard().getConsecutiveCorrectCount()));

        //Prepare play of pronunciation
        Commander.setState(PREFS_PLAY_PRONUNCIATION,
                PlayPronunciationState.builder()
                        .contextWrapper(new ContextWrapper(getContext()))
                        .flashCard(review.getFlashCard())
                        .build()
        );
        Commander.run(PREFS_PLAY_PRONUNCIATION);

        //FEATURE: Play review animation
        Commander.setState(PREFS_DISPLAY_ANIMATION,
                ReviewAnimationState.builder().binding(binding).hasFailed(review.hasFailed()).build());
        Commander.run(PREFS_DISPLAY_ANIMATION);


        /*  when a flashCard fails during review add it on top of the list stack.
            This causes the review to be finished only if all items have passed correctly.
         */
        model.getFlashCards().observe(getViewLifecycleOwner(), flashCards -> {
            if(flashCards.size() > 0){
                flashCards.remove(0);

                if (review.hasFailed()){
                    flashCards.add(review.getFlashCard());
                    //FEATURE: Shuffle based on preference
                    Commander.setState(PREFS_REVIEW_SHUFFLE,flashCards);
                    Commander.run(PREFS_REVIEW_SHUFFLE);
                }

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

    private String prepareSynonyms(Review review){
        String synonyms = "";
        List<String> synonymList = review.getFlashCard().getSynonyms();
        if(synonymList.size() > 0){
            for (int i=0; i<synonymList.size(); i++){
                //no comma at last synonym
                if(i<synonymList.size()-1)
                    synonyms += synonymList.get(i) +", ";
                else
                    synonyms += synonymList.get(i);
            }
        }
        return synonyms;
    }
}

