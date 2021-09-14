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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartingScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_REVIEW = 1;

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


        // Getting a result from an activity. This replaces startActivityForResult!
        ActivityResultLauncher<Intent> reviewResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data =  result.getData();
                            int position = data.getIntExtra(ReviewActivity.EXTRA_REMAINING_REVIEWS,0);
                            tv_reviewItemCount.setText("Position: " + position);

                        }
                    }
                });

        btnStartReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingScreenActivity.this, ReviewActivity.class);
                reviewResultLauncher.launch(intent);
            }
        });
    }

    private void showReviewItemCount(){
        if(model == null){
            model = new ViewModelProvider(this).get(StartingScreenViewModel.class);
            model.getNotes().observe(this, item -> {
                tv_reviewItemCount.setText("Review: " + item.size());
            });
        }
    }

}