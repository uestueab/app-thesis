package com.thesis.yatta;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.BarHighlighter;
import com.thesis.yatta.commander.Commander;
import com.thesis.yatta.commander.commands.ShowDiagramCommand;
import com.thesis.yatta.commander.state.ShowDiagramState;
import com.thesis.yatta.databinding.ActivityStartingScreenBinding;
import com.thesis.yatta.listeners.onClick.StartActivityListener;
import com.thesis.yatta.model.entity.FlashCard;
import com.thesis.yatta.viewmodel.StartingScreenViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.thesis.yatta.constants.ConstantsHolder.*;

public class StartingScreenActivity extends BaseActivity {
    private static final String TAG = "StartingScreenActivity";

    private ActivityStartingScreenBinding binding;
    private StartingScreenViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showReviewItemCount();

        model.getPastReviews().observe(this, pastReviews ->{
            Commander.init();
            Commander.setCommand(PREFS_SHOW_DIAGRAM,new ShowDiagramCommand());
            Commander.setState(PREFS_SHOW_DIAGRAM, ShowDiagramState.builder()
                    .binding(binding)
                    .context(this)
                    .pastReviews(new ArrayList<>(pastReviews))
                    .build()
            );
            Commander.run(PREFS_SHOW_DIAGRAM);
        });

        //Show diagram

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
        List<FlashCard> previousFlashCards = PrefManager.getFlashCards(PREFS_REMAINING_NOTES);

        if (previousFlashCards == null || previousFlashCards.size() == 0) {
            model = new ViewModelProvider(this).get(StartingScreenViewModel.class);
            model.getFlashCards().observe(this, item -> {
                int flashCardCount = model.getFlashCardsCount();
                binding.tvReviewItemCount.setText("Review: " + flashCardCount);
                if(flashCardCount > 0){
                    binding.btnStartReview.setVisibility(View.VISIBLE);
                }
                else{ // when there are no review flashCards available, there is no point in moving to review activity.
                    binding.btnStartReview.setVisibility(View.GONE);
                }
            });
        } else {
            binding.tvReviewItemCount.setText("Review: " + previousFlashCards.size());
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
                Intent intentSettings = new Intent(StartingScreenActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            case R.id.menu_item_manage_flashCards:
                Intent intentManageFlashCard = new Intent(StartingScreenActivity.this, ManageFlashCardActivity.class);
                startActivity(intentManageFlashCard);
                return true;
            case R.id.menu_item_allow_sound:
                PrefManager.init(this);
                if(!PrefManager.contains(PREFS_PLAY_PRONUNCIATION)){
                    //Never pressed the sound icon, so initially press should disable sound and switch icons accordingly
                    PrefManager.set(PREFS_PLAY_PRONUNCIATION, false);
                    item.setIcon(R.drawable.no_play_sound);
                }else{
                    Boolean sound_enabled = PrefManager.get(PREFS_PLAY_PRONUNCIATION,false);
                    if(sound_enabled){
                        PrefManager.set(PREFS_PLAY_PRONUNCIATION, false);
                        item.setIcon(R.drawable.no_play_sound);
                    }else{
                        PrefManager.set(PREFS_PLAY_PRONUNCIATION, true);
                        item.setIcon(R.drawable.play_sound);
                    }
                }
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