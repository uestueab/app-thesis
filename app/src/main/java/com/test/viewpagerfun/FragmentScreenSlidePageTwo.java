package com.test.viewpagerfun;

import android.content.Intent;
import android.os.Bundle;

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
    private Button btn_nextTop;
    private Button btn_nextBottom;

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
        btn_nextTop = getView().findViewById(R.id.btn_nextTop);
        btn_nextBottom = getView().findViewById(R.id.btn_nextBottom);
        btn_nextBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_nextTop.performClick();
            }
        });

        //Update the UI.
        model.getNote().observe(getViewLifecycleOwner(), item -> {
            tv_question.setText(item.getTitle());
        });


        //Move to next Item
        btn_nextTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the next note, if one exists
                if (model.hasNextNote()) {
                    ((ReviewActivity) getActivity()).previous_fragment();
                    tv_question.setText("");

                } else {
                    // all items passed, quit by moving to another activity
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

}
