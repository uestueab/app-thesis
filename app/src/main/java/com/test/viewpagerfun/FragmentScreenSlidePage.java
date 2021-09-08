package com.test.viewpagerfun;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentScreenSlidePage extends Fragment {

    private static final String TAG = "FragmentScreenSlidePage";

    //make communication between fragments possible
    private SharedViewModel model;

    //UI components
    private TextView tv_note;
    private Button btn_next;

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

        //
        tv_note = (TextView) getView().findViewById(R.id.tv_question);
        btn_next = (Button) getView().findViewById(R.id.btn_next);

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        observePosition();
        model.getNote().observe(getViewLifecycleOwner(), item -> {
            // Update the UI.
            tv_note.setText(item.getQuestion());
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ReviewActivity)getActivity()).nextFragment();
                //removes glichty effect on fragment switch
                tv_note.setText("");
            }
        });

        observePosition();
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