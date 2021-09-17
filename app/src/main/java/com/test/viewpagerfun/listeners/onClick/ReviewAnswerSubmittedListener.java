package com.test.viewpagerfun.listeners.onClick;

import android.app.Activity;
import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.test.viewpagerfun.ReviewActivity;
import com.test.viewpagerfun.databinding.ReviewInputFragmentBinding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static android.content.Context.VIBRATOR_SERVICE;

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

    @Override
    public void onClick(View v) {
        if (binding.etReviewAnswer.getText().toString().equals(binding.tvQuestion.getText())) {
            ((ReviewActivity) getActivity()).nextFragment();
            //removes glitch effect on fragment switch
            binding.tvQuestion.setText("");
            binding.etReviewAnswer.setText("");
        } else {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            vibrateOnError();
            binding.etReviewAnswer.startAnimation(shakeError());
        }
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
