package com.thesis.yokatta;

import androidx.lifecycle.ViewModelProvider;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.thesis.yokatta.commander.Commander;
import com.thesis.yokatta.commander.commands.ShowDiagramCommand;
import com.thesis.yokatta.commander.commands.ShowNotificationCommand;
import com.thesis.yokatta.commander.receiver.ShowNotification;
import com.thesis.yokatta.commander.state.ShowDiagramState;
import com.thesis.yokatta.commander.state.ShowNotificationState;
import com.thesis.yokatta.databinding.ActivityStartingScreenBinding;
import com.thesis.yokatta.listeners.onClick.StartActivityListener;
import com.thesis.yokatta.model.entity.FlashCard;
import com.thesis.yokatta.model.entity.PastReview;
import com.thesis.yokatta.toolbox.TimeProvider;
import com.thesis.yokatta.viewmodel.StartingScreenViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.thesis.yokatta.constants.ConstantsHolder.*;

public class StartingScreenActivity extends BaseActivity {
    private static final String TAG = "StartingScreenActivity";

    private ActivityStartingScreenBinding binding;
    private StartingScreenViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityStartingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(StartingScreenViewModel.class);
        showReviewItemCount();

        PrefManager.init(this);
        //defaultValues less than 0L implies first launch, in any other case get date of last usage
        Long lastLaunched = PrefManager.get(PREFS_LAST_LAUNCHED, -1L);

        if(lastLaunched > 0L)
            binding.tvLastLaunchedInfo.setText("Last visited: \t" + TimeProvider.toHumanReadableDate(lastLaunched));
         else
            binding.tvLastLaunchedInfo.setText("Welcome, nice to meet you!");

        //observe livedata for diagram
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

        //launch the review
        binding.btnStartReview.setOnClickListener(
                StartActivityListener.builder()
                        .currentActivity(this)
                        .targetActivity(ReviewActivity.class)
                        .build());

        //Notifications (after some time of inactivity)
        Commander.init();
        Commander.setCommand(PREFS_GENERAL_NOTIFICATIONS,new ShowNotificationCommand());
        Commander.setState(PREFS_GENERAL_NOTIFICATIONS, new ShowNotificationState(StartingScreenActivity.this));
        Commander.run(PREFS_GENERAL_NOTIFICATIONS);
    }

    /*
     *  Display how many items need to be reviewed. Either:
     *  - New items. (loaded from the database)
     *  - Remaining items from a previous session. (loaded from SharedPreferences)
     */
    private void showReviewItemCount() {
        PrefManager.init(this);
        List<FlashCard> previousFlashCards = PrefManager.getFlashCards(PREFS_REMAINING_FLASH_CARDS);

        Boolean reviewExists = PrefManager.get("PREFS_REVIEW_EXISTS",false);
        //no left over cards means: -> check if there are new cards due for the review
        if (previousFlashCards == null || previousFlashCards.size() == 0) {
            model.getFlashCards().observe(this, item -> {
                int flashCardCount = item.size();
                binding.tvReviewItemCount.setText("Review: " + flashCardCount);
                if(flashCardCount > 0){
                    binding.btnStartReview.setVisibility(View.VISIBLE);

                    //makes sure there is only ONE record in pastReviews table that has no end date yet.
                    if (!reviewExists){
                        model.insert(PastReview.builder().itemCount(flashCardCount).build());
                        PrefManager.set("PREFS_REVIEW_EXISTS",true);
                    }
                }
                else{ //when there are no review flashCards available, there is no point in moving to review activity.
                    binding.tvReviewItemCount.setText("No reviews available yet.");
                    binding.btnStartReview.setVisibility(View.GONE);
                    //any review here is passed completely, that's why we can give the review an end date
                    model.updateReviewHasEnded(TimeProvider.now());
                    PrefManager.set("PREFS_REVIEW_EXISTS",false);
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
                toggleSound(item);
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
        //updates the textview info about when the app was launched last
        PrefManager.set(PREFS_LAST_LAUNCHED,TimeProvider.now());
    }

    private void toggleSound(MenuItem item){
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
    }
}