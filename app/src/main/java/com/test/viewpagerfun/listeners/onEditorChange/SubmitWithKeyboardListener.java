package com.test.viewpagerfun.listeners.onEditorChange;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.test.viewpagerfun.databinding.ReviewInputFragmentBinding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
/*
 *  Makes it possible to submit an answer by hitting the Enter key on the keyboard,
 *  in conjunction with submitting with pressing on the button.
 */
public class SubmitWithKeyboardListener implements TextView.OnEditorActionListener {

    private ReviewInputFragmentBinding binding;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            binding.btnSubmit.performClick();
            return true;
        }
        return false;
    }
}
