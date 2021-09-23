package com.test.viewpagerfun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.viewpagerfun.databinding.ActivityStartingScreenBinding;
import com.test.viewpagerfun.listeners.onClick.StartActivityListener;
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

        //launch the review
        binding.btnStartReview.setOnClickListener(
                StartActivityListener.builder()
                        .currentActivity(this)
                        .targetActivity(ReviewActivity.class)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_cards:
                Toast.makeText(this, "pressed: search_cards", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_all_notes:
                Intent intent = new Intent(StartingScreenActivity.this, ManageNoteActivity.class);
                startActivity(intent);
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }

}