package com.test.viewpagerfun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingScreenActivity extends AppCompatActivity {

    private Button btnStartReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        btnStartReview = findViewById(R.id.btn_startReview);
        btnStartReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReview();
            }
        });


    }

    private void startReview(){
        Intent intent = new Intent(StartingScreenActivity.this, ReviewActivity.class);
        startActivity(intent);
    }
}