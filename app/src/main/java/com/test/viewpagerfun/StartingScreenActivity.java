package com.test.viewpagerfun;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.test.viewpagerfun.databinding.ActivityStartingScreenBinding;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.viewmodel.StartingScreenViewModel;

import java.util.List;

public class StartingScreenActivity extends AppCompatActivity {

    private StartingScreenViewModel model;

    private ActivityStartingScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

                            Intent intent = result.getData();
                            Bundle bundle = intent.getBundleExtra(ReviewActivity.EXTRA_REMAINING_REVIEWS);
                            List<Note> notes = (List<Note>) bundle.getSerializable("notes");

                            //persist any remaining items to sharedpreferences
                            if (notes.size() > 0) {
                                new PrefManager<>(getApplicationContext()).setNotes("REMAINING_NOTES", notes);
                            } else {
                                //or clear the key-value pair
                                new PrefManager<>(getApplication()).remove("REMAINING_NOTES");
                            }
                            binding.tvReviewItemCount.setText("Review: " + notes.size());
                        }
                    }
                });

        //launch the review
        binding.btnStartReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingScreenActivity.this, ReviewActivity.class);
                reviewResultLauncher.launch(intent);
            }
        });
    }

    private void showReviewItemCount() {
        List<Note> previousNotes = new PrefManager<>(getApplicationContext()).getNotes("REMAINING_NOTES");

        if (previousNotes == null || previousNotes.size() == 0) {
            model = new ViewModelProvider(this).get(StartingScreenViewModel.class);
            model.getNotes().observe(this, item -> {
                binding.tvReviewItemCount.setText("Review: " + item.size());
            });
        } else {
            binding.tvReviewItemCount.setText("Review: " + previousNotes.size());
        }
    }

}