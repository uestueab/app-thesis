package com.thesis.yokatta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.thesis.yokatta.commander.Commander;
import com.thesis.yokatta.commander.commands.BackPressCommand;
import com.thesis.yokatta.commander.state.BackPressState;
import com.thesis.yokatta.model.entity.FlashCard;
import com.thesis.yokatta.sm2.Scheduler;
import com.thesis.yokatta.sm2.Session;
import com.thesis.yokatta.viewmodel.SharedViewModel;
import com.thesis.yokatta.viewmodel.SharedViewModelFactory;

import java.util.List;

import static com.thesis.yokatta.constants.ConstantsHolder.*;

public class ReviewActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private final Handler handler = new Handler();

    // make use of animations, when moving to next fragment
    private ViewPager2 viewPager;

    private SharedViewModel model;

    // register the time the back button was pressed
    private long backPressedTime;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get reference of ViewPager2 and the PagerAdapter.
        viewPager = findViewById(R.id.pager);
        // use button navigation, instead of gesture swiping to next fragment
        viewPager.setUserInputEnabled(false);

        // The pager adapter, which provides the pages to the view pager widget.
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Prepare features associated with this activity, after inflating layout
        Commander.init();
        Commander.setCommand(PREFS_REVIEW_BACK, new BackPressCommand());
        Commander.setState(PREFS_REVIEW_BACK, new BackPressState(this));

        resumeReview();
    }

    private void resumeReview() {
        PrefManager.init(this);
        List<FlashCard> previousFlashCards = PrefManager.getFlashCards(PREFS_REMAINING_FLASH_CARDS);

        /* - If no flashCards from a previous review exist. Load new review items from database
         * - Else restore review with remaining items.
         */
        if (previousFlashCards == null || previousFlashCards.size() == 0) {
            //The ViewModelFactory makes it possible to call different constructors for the viewmodel.
            model = new ViewModelProvider(this, new SharedViewModelFactory(getApplication())).get(SharedViewModel.class);
        } else {
            model = new ViewModelProvider(this,
                    new SharedViewModelFactory(getApplication(), previousFlashCards)).get(SharedViewModel.class);
        }
    }

    public void nextFragment() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void previous_fragment() {
        //setting smoothScroll to false disables viewpager animation.
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, false);
    }

    @Override
    public void onBackPressed() {
        /*
            Feature: Avoid accidentally going out of review by hitting the back button.
            Instead leave review only when back button was pressed in quick succession.
         */
        Commander.run(PREFS_REVIEW_BACK);
    }

    /* Things to be done, when the activity loses foreground state
     * basically, when:
     *                      - putting app in the background
     *                      - moving to another activity through intent.
     *
     * do: save current progress in the review session.
     */
    @Override
    protected void onPause() {
        super.onPause();

        handler.post(new Runnable() {
            @Override
            public void run() {
                List<FlashCard> remainingFlashCards = model.getRemainingFlashCards();
                PrefManager.init(ReviewActivity.this);
                PrefManager.setFlashCards(PREFS_REMAINING_FLASH_CARDS, remainingFlashCards);
            }
        });

        /*
            SM2: Apply algorithm & Update the database
         */
        Scheduler scheduler = Scheduler.builder().build();
        Session session = model.getSession();
        scheduler.applySession(session);

        for (FlashCard flashCard : session.getFlashCardStatistics().keySet())
            model.update(flashCard);

        model.resetSession();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}