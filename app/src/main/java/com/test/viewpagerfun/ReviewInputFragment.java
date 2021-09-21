package com.test.viewpagerfun;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.viewpagerfun.databinding.ReviewInputFragmentBinding;
import com.test.viewpagerfun.listeners.onClick.ReviewAnswerSubmittedListener;
import com.test.viewpagerfun.listeners.onEditorChange.SubmitWithKeyboardListener;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.sm2.Review;
import com.test.viewpagerfun.viewmodel.SharedViewModel;

import java.util.List;

public class ReviewInputFragment extends Fragment {

    //make communication between fragments possible
    private SharedViewModel model;

    //view binding of fragment
    private ReviewInputFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //inflate the layout
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = ReviewInputFragmentBinding.inflate(inflater, container, false);
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
        model.getNotes().observe(getViewLifecycleOwner(), notes -> {
            binding.tvQuestion.setText(model.getNoteAtPosition().getPrompt());
        });

        answerSubmitted();

        observePosition();
    }

    /*  User submits an answer:
     *  - allow submitting with enter key on keyboard.
     *  - check if field is empty and handle correct answer..
     */
    private void answerSubmitted() {
        binding.etReviewAnswer.setOnEditorActionListener(
                SubmitWithKeyboardListener.builder()
                        .binding(binding)
                        .build()
        );

        binding.btnSubmit.setOnClickListener(
                ReviewAnswerSubmittedListener.builder()
                        .activity(getActivity())
                        .model(model)
                        .binding(binding)
                        .build()
        );
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