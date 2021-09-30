package com.test.viewpagerfun.listeners.onClick;

import android.app.ActionBar;
import android.app.Activity;
import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.viewpagerfun.PrefManager;
import com.test.viewpagerfun.R;
import com.test.viewpagerfun.ReviewActivity;
import com.test.viewpagerfun.commander.Commander;
import com.test.viewpagerfun.commander.commands.HapticFeedbackCommand;
import com.test.viewpagerfun.commander.commands.MismatchToastCommand;
import com.test.viewpagerfun.commander.state.MismatchToastState;
import com.test.viewpagerfun.databinding.ReviewInputFragmentBinding;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.sm2.Review;
import com.test.viewpagerfun.toolbox.Levenshtein;
import com.test.viewpagerfun.toolbox.StringProvider;
import com.test.viewpagerfun.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.test.viewpagerfun.constants.ConstantsHolder.*;

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

        Note note = model.getNote();
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
        if(isValidAnswer(response,note)){
            // no hesitation, full score
            if(timeSpend <= 30) { score = 3; }
            else { // hesitated, adjust score
                score = 2;
            }
        }else{ score = 1; }

        review = new Review(note,score);
        Log.d(TAG, "onClick: "+ "[Review score: " + score + "]");

        // Note is now reviewed
        model.setMostRecentReview(review);
        model.applyReview(review);

        ((ReviewActivity) getActivity()).nextFragment();
        //removes glitch effect on fragment switch
        binding.tvQuestion.setText("");
        binding.etReviewAnswer.setText("");
    }

    private boolean isValidAnswer(String response, Note note){
        String meaning = StringProvider.toComparable(note.getMeaning());
        return response.equals(meaning) || withinTolerance(response, note);
    }

    private boolean withinTolerance(String response, Note note){
        List<String> meanings;
        List<String> synonyms = note.getSynonyms();
        if(synonyms != null)
             meanings = new ArrayList<>(synonyms);
        else
            meanings = new ArrayList<>();
        meanings.add(note.getMeaning());

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
