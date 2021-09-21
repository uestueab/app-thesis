package com.test.viewpagerfun.listeners.onClick;

import android.app.Activity;
import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.test.viewpagerfun.ReviewActivity;
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

    private Activity activity;
    private ReviewInputFragmentBinding binding;
    private SharedViewModel model;

    @Override
    public void onClick(View v) {

        String rawUserResponse = binding.etReviewAnswer.getText().toString();
        String response = StringProvider.toComparable(rawUserResponse);

        Note note = model.getNoteAtPosition();
        Review review;

        if (response.length() == 0) {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            vibrateOnError();
            binding.etReviewAnswer.startAnimation(shakeError());

            return;
        }

        if(isValidAnswer(response,note)){
            review = new Review(note,3);
        }else{
            review = new Review(note,1);
        }

        // Note is now reviewed
        model.setReview(review);

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
        List<String> meanings = note.getSynonyms();
        //there can be notes without synonyms, in that case initialize a new arraylist.
        if(meanings == null)
            meanings = new ArrayList<>();
        //list that contains all possible correct answers.
        meanings.add(note.getMeaning());

        for(String meaning : meanings){
            int distance = Levenshtein.distance(response,meaning);
            int mismatchTolerance = (int) Math.floor(meaning.length() / MIN_MISMATCH_LENGTH);

            if(distance <= mismatchTolerance)
                return true;
        }
        return false;
    }


    private TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 20, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(4));
        return shake;
    }

    private void vibrateOnError() {
        Vibrator v = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        VibrationEffect ve = VibrationEffect.createOneShot(100,
                VibrationEffect.DEFAULT_AMPLITUDE);
        v.vibrate(ve, audioAttributes);
    }
}
