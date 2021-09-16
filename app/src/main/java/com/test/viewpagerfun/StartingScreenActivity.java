package com.test.viewpagerfun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.viewpagerfun.model.entity.Note;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StartingScreenActivity extends AppCompatActivity {

    private StartingScreenViewModel model;

    private TextView tv_reviewItemCount;
    private Button btnStartReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        tv_reviewItemCount = findViewById(R.id.tv_reviewItemCount);
        btnStartReview = findViewById(R.id.btn_startReview);

        showReviewItemCount();

        /* Getting a result from an activity. This replaces startActivityForResult!
         * More specifically, this gets triggered when ReviewActivity sends back remaining notes
         * whenever the back button was pressed.
         */
        ActivityResultLauncher<Intent> reviewResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Intent intent =  result.getData();
                            Bundle bundle = intent.getBundleExtra(ReviewActivity.EXTRA_REMAINING_REVIEWS);
                            List<Note> notes = (List<Note>) bundle.getSerializable("notes");

                            if(notes.size() > 0) {
                                new PrefManager<Note>(getApplicationContext()).setList("REMAINING_NOTES",notes);
                            }else{
                                new PrefManager<>(getApplication()).remove("REMAINING_NOTES");
                            }
                            tv_reviewItemCount.setText("Review: " + notes.size());
                        }
                    }
                });

        //launch the review
        btnStartReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingScreenActivity.this, ReviewActivity.class);
                reviewResultLauncher.launch(intent);
            }
        });
    }

    private void showReviewItemCount(){
        List<Note> previousNotes  = new PrefManager<Note>(getApplicationContext())
                .getList("REMAINING_NOTES");


        if(previousNotes == null || previousNotes.size() == 0){
            model = new ViewModelProvider(this).get(StartingScreenViewModel.class);
            model.getNotes().observe(this, item -> {
                tv_reviewItemCount.setText("Review: " + item.size());
            });
        }else{
            tv_reviewItemCount.setText("Review: " + previousNotes.size());
        }
    }

}