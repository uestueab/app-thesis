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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentScreenSlidePageTwo extends Fragment {

    private SharedViewModel model;

    private TextView tv_question;
    private Button btn_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page_two,
                container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        tv_question = getView().findViewById(R.id.tv_question);
        btn_back = getView().findViewById(R.id.btn_back);

        //Update the UI.
        model.getNote().observe(getViewLifecycleOwner(), item -> {
            tv_question.setText(item.getQuestion());
        });


        //Move to next Item
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the next note, if one exists
                if (model.hasNextNote()) {
                    ((MainActivity) getActivity()).previous_fragment();
                } else {    //all notes are passed, do something..
                    Toast.makeText(getActivity(), "Review ended", Toast.LENGTH_SHORT).show();
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

}
