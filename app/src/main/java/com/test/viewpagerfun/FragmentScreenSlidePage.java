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
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.VIBRATOR_SERVICE;

public class FragmentScreenSlidePage extends Fragment {

    private static final String TAG = "FragmentScreenSlidePage";

    //make communication between fragments possible
    private SharedViewModel model;

    //UI components
    private TextView tv_note;
    private EditText et_reviewAnswer;
    private Button btn_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        return (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page,
                container, false);
    }

    /* This is the only callback method being triggered when:
     *  - switching from FragmentScreenSlidePageTwo to this fragment (back button)
     *  - putting the app in background.
     */
    @Override
    public void onResume() {
        super.onResume();

        tv_note = (TextView) getView().findViewById(R.id.tv_question);
        btn_submit = (Button) getView().findViewById(R.id.btn_submit);
        et_reviewAnswer = (EditText) getView().findViewById(R.id.et_reviewAnswer);
        focusOnInputArea(et_reviewAnswer);

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        observePosition();
        model.getNote().observe(getViewLifecycleOwner(), item -> {
            // Update the UI.
            tv_note.setText(item.getQuestion());
        });

        answerSubmitted();
        et_reviewAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_submit.performClick();
                    return true;
                }
                return false;
            }
        });

        observePosition();
    }

    private void answerSubmitted(){
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_reviewAnswer.getText().toString().equals(tv_note.getText())){
                    ((ReviewActivity)getActivity()).nextFragment();
                    //removes glichty effect on fragment switch
                    tv_note.setText("");
                }else {
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    vibrateOnError();
                    et_reviewAnswer.startAnimation(shakeError());

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

    private void vibrateOnError(){
        Vibrator v = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        VibrationEffect ve = VibrationEffect.createOneShot(100,
                VibrationEffect.DEFAULT_AMPLITUDE);
        v.vibrate(ve, audioAttributes);
    }

    private void focusOnInputArea(EditText et){
        et_reviewAnswer.post(new Runnable() {
            @Override
            public void run() {
                et.isFocusableInTouchMode();
                et.setFocusable(true);
                et.requestFocus();
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
}