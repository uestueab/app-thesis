package com.test.viewpagerfun;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.test.viewpagerfun.databinding.FragmentScreenSlidePageTwoBinding;
import com.test.viewpagerfun.viewmodel.SharedViewModel;


public class ReviewDetailedResultFragment extends Fragment {

    private static  String TAG;

    //make communication between fragments possible
    private SharedViewModel model;
    //view binding of fragment
    private FragmentScreenSlidePageTwoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScreenSlidePageTwoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.btnNextBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnNextTop.performClick();
            }
        });

        //Update the UI.
        model.getNote().observe(getViewLifecycleOwner(), item -> {
            binding.tvQuestion.setText(item.getTitle());
        });

        //Decides finishing the review, or showing next item in queue.
        binding.btnNextTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //let move to the review user input fragment if remaining notes exist
                if (model.hasNextNote()) {
                    ((ReviewActivity) getActivity()).previous_fragment();
                    //remove flicker
                    binding.tvQuestion.setText("");
                } else { // all items passed, quit by moving to another activity
                    new PrefManager<>(getActivity()).remove("REMAINING_NOTES");

                    Intent intent = new Intent(getActivity(), StartingScreenActivity.class);
                    startActivity(intent);
                }
            }
        });
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

