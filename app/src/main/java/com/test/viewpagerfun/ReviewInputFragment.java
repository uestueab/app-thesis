package com.test.viewpagerfun;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.test.viewpagerfun.databinding.FragmentScreenSlidePageBinding;
import com.test.viewpagerfun.viewmodel.SharedViewModel;

import static android.content.Context.VIBRATOR_SERVICE;

public class ReviewInputFragment extends Fragment {

    //make communication between fragments possible
    private SharedViewModel model;

    //view binding of fragment
    private FragmentScreenSlidePageBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //inflate the layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScreenSlidePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /* This is the only callback method being triggered when:
     *  - switching from ReviewDetailedResultFragment to this fragment (back button)
     *  - putting the app in background.
     */
    @Override
    public void onResume() {
        super.onResume();

        focusOnInputArea(binding.etReviewAnswer);

        //get the SharedViewModel which is scoped to the underlying activity.
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Update the UI.
        model.getNotes().observe(getViewLifecycleOwner(), item -> {
            binding.tvQuestion.setText(item.get(model.getPosition().getValue()).getTitle());
        });

        answerSubmitted();
        observePosition();
    }

    /*  User submits an answer:
     *  - allow submitting with enter key on keyboard.
     *  - check if field is empty and handle correct answer..
     */
    private void answerSubmitted() {
        binding.etReviewAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.btnSubmit.performClick();
                    return true;
                }
                return false;
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etReviewAnswer.getText().toString().equals(binding.tvQuestion.getText())) {
                    ((ReviewActivity) getActivity()).nextFragment();
                    //removes glichty effect on fragment switch
                    binding.tvQuestion.setText("");
                    binding.etReviewAnswer.setText("");
                } else {
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    vibrateOnError();
                    binding.etReviewAnswer.startAnimation(shakeError());
                }
            }
        });
    }

    private TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 20, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(4));
        return shake;
    }

    private void vibrateOnError() {
        Vibrator v = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        VibrationEffect ve = VibrationEffect.createOneShot(100,
                VibrationEffect.DEFAULT_AMPLITUDE);
        v.vibrate(ve, audioAttributes);
    }

    //focus on the given edittext and popup the keyboard
    private void focusOnInputArea(EditText et) {
        //put the runnable at the end of the event queue
        binding.etReviewAnswer.post(new Runnable() {
            @Override
            public void run() {
                et.isFocusableInTouchMode();
                et.setFocusable(true);
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private void observePosition() {
        model.getPosition()
                .observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                    }
                });
    }

    //Fragments outlive their views. clean up any references to the binding class instance in the fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}