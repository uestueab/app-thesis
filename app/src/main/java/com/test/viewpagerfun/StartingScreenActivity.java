package com.test.viewpagerfun;

import androidx.lifecycle.ViewModelProvider;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.test.viewpagerfun.databinding.ActivityStartingScreenBinding;
import com.test.viewpagerfun.listeners.onClick.StartActivityListener;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.model.repository.NoteRepository;
import com.test.viewpagerfun.viewmodel.StartingScreenViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class StartingScreenActivity extends BaseActivity {
    private static final String TAG = "StartingScreenActivity";
    private ActivityStartingScreenBinding binding;
    private StartingScreenViewModel model;

    private final Handler handler = new Handler();

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

    public void scheduleJob(int count) {

        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt("count", count);

        ComponentName componentName = new ComponentName(this, NotificationJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setExtras(bundle)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }

    /*
     *  Display how many items need to be reviewed. Either:
     *  - New items. (loaded from the database)
     *  - Remaining items from a previous session. (loaded from SharedPreferences)
     */
    private void showReviewItemCount() {
        PrefManager.init(this);
        List<Note> previousNotes = PrefManager.getNotes(PREFS_REMAINING_NOTES);

        if (previousNotes == null || previousNotes.size() == 0) {
            model = new ViewModelProvider(this).get(StartingScreenViewModel.class);
            model.getNotesLiveData().observe(this, item -> {
                binding.tvReviewItemCount.setText("Review: " + item.size());
                scheduleJob(item.size());
            });
        } else {
            binding.tvReviewItemCount.setText("Review: " + previousNotes.size());
        }
    }

    /*
     *  Create the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.startingscreen_menu, menu);
        return true;
    }

    /*
     *  Decides what to do when pressed on a menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_settings:
                Intent intent2 = new Intent(StartingScreenActivity.this, SettingsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.menu_item_manage_notes:
                Intent intent = new Intent(StartingScreenActivity.this, ManageNoteActivity.class);
                startActivity(intent);
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    /*
     *  Moving out of app via back press is prohibited
     */
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        PrefManager.init(this);
        PrefManager.set(NOTIFICATIONS_LAST_RUN,System.currentTimeMillis());
    }
}