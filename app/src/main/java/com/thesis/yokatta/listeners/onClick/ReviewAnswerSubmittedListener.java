package com.thesis.yokatta.listeners.onClick;

import android.app.Activity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.thesis.yokatta.ReviewActivity;
import com.thesis.yokatta.commander.Commander;
import com.thesis.yokatta.commander.commands.HapticFeedbackCommand;
import com.thesis.yokatta.commander.commands.MismatchToastCommand;
import com.thesis.yokatta.commander.state.MismatchToastState;
import com.thesis.yokatta.databinding.ReviewInputFragmentBinding;
import com.thesis.yokatta.model.entity.FlashCard;
import com.thesis.yokatta.sm2.Review;
import com.thesis.yokatta.toolbox.Levenshtein;
import com.thesis.yokatta.toolbox.StringProvider;
import com.thesis.yokatta.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.thesis.yokatta.constants.ConstantsHolder.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
/*
 *  Process the input submitted.
 *  Add some feedback, like small vibrate when empty string submitted.
 */
public class ReviewAnswerSubmittedListener implements View.OnClickListener{
    private static final String TAG = "ReviewAnswerSubmittedListener";

    private Activity activity;
    private ReviewInputFragmentBinding binding;
    private SharedViewModel model;

    private long startTime;

    @Override
    public void onClick(View v) {

        long timeSpend = (System.currentTimeMillis() - startTime) / 1000;

        String rawUserResponse = binding.etReviewAnswer.getText().toString();
        String response = StringProvider.toComparable(rawUserResponse);

        FlashCard flashCard = model.getFlashCard();
        Review review;

        //Prepare features for 'answer submitted' event
        Commander.init();
        Commander.setCommand(PREFS_REVIEW_HAPTIC, new HapticFeedbackCommand());
        Commander.setCommand(PREFS_REVIEW_MISMATCH_TOAST, new MismatchToastCommand());

        if (response.length() == 0) {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            // Feature: Vibrate when empty answer submitted
            Commander.setState(PREFS_REVIEW_HAPTIC, activity);
            Commander.run(PREFS_REVIEW_HAPTIC);
            binding.etReviewAnswer.startAnimation(shakeError());

            return;
        }

        int score;

        //Determine score for the review
        if(isValidAnswer(response,flashCard)){
            // no hesitation, full score
            if(timeSpend <= 30) { score = 3; }
            else { // hesitated, adjust score
                score = 2;
            }
            model.incrementCorrectCount();
        }else{ score = 1; }

        review = new Review(flashCard,score);
        Log.d(TAG, "onClick: "+ "[Review score: " + score + "]");

        // FlashCard is now reviewed
        model.setMostRecentReview(review);
        model.applyReview(review);

        ((ReviewActivity) getActivity()).nextFragment();
        //removes glitch effect on fragment switch
        binding.tvQuestion.setText("");
        binding.etReviewAnswer.setText("");
    }

    private boolean isValidAnswer(String response, FlashCard flashCard){
        String meaning = StringProvider.toComparable(flashCard.getMeaning());
        return response.equals(meaning) || withinTolerance(response, flashCard);
    }

    private boolean withinTolerance(String response, FlashCard flashCard){
        List<String> meanings;
        List<String> synonyms = flashCard.getSynonyms();
        if(synonyms != null)
             meanings = new ArrayList<>(synonyms);
        else
            meanings = new ArrayList<>();
        meanings.add(flashCard.getMeaning());

        for(String meaning : meanings){
            int distance = Levenshtein.distance(response,meaning);
            int mismatchTolerance = (int) Math.floor(meaning.length() / MIN_MISMATCH_LENGTH);

            if(distance <= mismatchTolerance){
                Commander.setState(PREFS_REVIEW_MISMATCH_TOAST,
                        MismatchToastState.builder().activity(activity).binding(binding).distance(distance).meaning(meaning).build());
                Commander.run(PREFS_REVIEW_MISMATCH_TOAST);

                return true;
            }
        }
        return false;
    }


    private TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 20, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(4));
        return shake;
    }
}
