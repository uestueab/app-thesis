package com.test.viewpagerfun;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.test.viewpagerfun.callbacks.backToStartingScreenCallback;
import com.test.viewpagerfun.databinding.ActivityStartingScreenBinding;
import com.test.viewpagerfun.listeners.onClick.StartReviewListener;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.viewmodel.StartingScreenViewModel;

import java.util.List;
import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class StartingScreenActivity extends AppCompatActivity {

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
                backToStartingScreenCallback.builder()
                        .context(getApplicationContext())
                        .binding(binding)
                        .build()
        );

        //launch the review
        binding.btnStartReview.setOnClickListener(
                StartReviewListener.builder()
                        .currentActivity(this)
                        .targetActivity(ReviewActivity.class)
                        .resultLauncher(reviewResultLauncher)
                        .build());
    }

    /*
     *  Display how many items need to be reviewed. Either:
     *  - New items. (loaded from the database)
     *  - Remaining items from a previous session. (loaded from SharedPreferences)
     */
    private void showReviewItemCount() {
        List<Note> previousNotes = new PrefManager<>(getApplicationContext()).getNotes(PREFS_REMAINING_NOTES);

        if (previousNotes == null || previousNotes.size() == 0) {
            StartingScreenViewModel model = new ViewModelProvider(this).get(StartingScreenViewModel.class);

            model.getNotes().observe(this, item -> {
                binding.tvReviewItemCount.setText("Review: " + item.size());
            });
        } else {
            binding.tvReviewItemCount.setText("Review: " + previousNotes.size());
        }
    }

}