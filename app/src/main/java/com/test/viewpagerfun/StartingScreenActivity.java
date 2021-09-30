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
import android.widget.Toast;

import com.test.viewpagerfun.databinding.ActivityStartingScreenBinding;
import com.test.viewpagerfun.listeners.onClick.StartActivityListener;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.viewmodel.StartingScreenViewModel;

import java.util.List;
import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class StartingScreenActivity extends BaseActivity {
    private static final String TAG = "StartingScreenActivity";
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


        scheduleJob(NOTIFY_DEFAULT_DELAY_TIME);
    }

    public void scheduleJob(long delayInMilliSec) {

        PersistableBundle bundle = new PersistableBundle();
        bundle.putLong(NOTIFY_DELAY_TIME, delayInMilliSec);

        ComponentName componentName = new ComponentName(this, NotificationJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setExtras(bundle)
                .setPersisted(true)
                .setPeriodic(delayInMilliSec)
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
            StartingScreenViewModel model = new ViewModelProvider(this).get(StartingScreenViewModel.class);
            model.getNotes().observe(this, item -> {
                int noteCount = model.getNotesCount();
                binding.tvReviewItemCount.setText("Review: " + noteCount);
                if(noteCount > 0){
                    binding.btnStartReview.setVisibility(View.VISIBLE);
                }
                else{ // when there are no review notes available, there is no point in moving to review activity.
                    binding.btnStartReview.setVisibility(View.GONE);
                }
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
        PrefManager.set(APP_CLOSED_AT,System.currentTimeMillis());
        Log.d(TAG, APP_CLOSED_AT + " value is set");
    }
}