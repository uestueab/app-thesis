package com.test.viewpagerfun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.viewpagerfun.model.datasource.NoteRepository;
import com.test.viewpagerfun.model.entity.Note;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StartingScreenActivity extends AppCompatActivity {

    private NoteRepository repository;

    private TextView tv_reviewItemCount;
    private Button btnStartReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        tv_reviewItemCount = findViewById(R.id.tv_reviewItemCount);
        btnStartReview = findViewById(R.id.btn_startReview);

        loadReviews();

        btnStartReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReview();
            }
        });


    }

    private void loadReviews(){
        if(repository == null) {
            repository = new NoteRepository(getApplication());
            repository.getAllNotesLiveData().observe(this, list -> {
                tv_reviewItemCount.setText("Review: " + list.size());
            });
        }
    }

    private void startReview(){
        Intent intent = new Intent(StartingScreenActivity.this, ReviewActivity.class);
        startActivity(intent);
    }
}