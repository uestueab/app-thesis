package com.test.viewpagerfun;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.test.viewpagerfun.databinding.ReviewDetailedResultFragmentBinding;
import com.test.viewpagerfun.listeners.onClick.NextReviewItemListener;
import com.test.viewpagerfun.viewmodel.SharedViewModel;
public class ReviewDetailedResultFragment extends Fragment {

    //make communication between fragments possible
    private SharedViewModel model;
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

    @Override
    public void onResume() {
        super.onResume();

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //Update the UI.
        model.getNote().observe(getViewLifecycleOwner(), item -> {
            binding.tvQuestion.setText(item.getTitle());
        });

        NextReviewItemListener nextReviewItemListener = NextReviewItemListener.builder()
                .activity(getActivity())
                .model(model)
                .binding(binding)
                .build();


        //Decides finishing the review, or showing next item in queue.
        binding.btnNextTop.setOnClickListener(nextReviewItemListener);
        binding.btnNextBottom.setOnClickListener(nextReviewItemListener);

        observePosition();
    }

    //observes changes of position from current fragment
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

