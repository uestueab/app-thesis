package com.thesis.yatta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.thesis.yatta.databinding.ReviewInputFragmentBinding;
import com.thesis.yatta.listeners.onClick.NotKnowListener;
import com.thesis.yatta.listeners.onClick.ReviewAnswerSubmittedListener;
import com.thesis.yatta.listeners.onEditorChange.SubmitWithKeyboardListener;
import com.thesis.yatta.model.entity.FlashCard;
import com.thesis.yatta.viewmodel.SharedViewModel;

public class ReviewInputFragment extends Fragment {

    //make communication between fragments possible
    private SharedViewModel model;

    //view binding of fragment
    private ReviewInputFragmentBinding binding;

    //measure time spend on this fragment. Determines whether the user hesitated during review
    long startTime;

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
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        focusOnInputArea(binding.etReviewAnswer);

        //get the SharedViewModel which is scoped to the underlying activity.
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Update the UI.
        model.getFlashCards().observe(getViewLifecycleOwner(), flashCards -> {
            FlashCard currentFlashCard = model.getFlashCard();
            if(currentFlashCard != null){
                binding.tvQuestion.setText(currentFlashCard.getPrompt());
                binding.tvReviewProgress.setText(model.getCorrectCount(false)+"/"+model.getTotalFlashCards());

                binding.btnNotKnow.setOnClickListener(
                        NotKnowListener.builder()
                                .context(getContext())
                                .mnemonic(currentFlashCard.getMnemonic())
                                .build()
                );

            }
        });

        startTime = System.currentTimeMillis();
        answerSubmitted();
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
                        .startTime(startTime)
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

    //Fragments outlive their views. clean up any references to the binding class instance in the fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}