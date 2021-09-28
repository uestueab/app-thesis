package com.test.viewpagerfun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.viewpagerfun.commander.Commander;
import com.test.viewpagerfun.commander.commands.BackPressCommand;
import com.test.viewpagerfun.commander.state.BackPressState;
import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.sm2.Scheduler;
import com.test.viewpagerfun.sm2.Session;
import com.test.viewpagerfun.viewmodel.SharedViewModel;
import com.test.viewpagerfun.viewmodel.SharedViewModelFactory;

import java.util.List;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

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
        List<Note> previousNotes = PrefManager.getNotes(PREFS_REMAINING_NOTES);

        /* - If no notes from a previous review exist. Load new review items from database
         * - Else restore review with remaining items.
         */
        if (previousNotes == null || previousNotes.size() == 0) {
            //The ViewModelFactory makes it possible to call different constructors for the viewmodel.
            model = new ViewModelProvider(this, new SharedViewModelFactory(getApplication())).get(SharedViewModel.class);
        } else {
            model = new ViewModelProvider(this,
                    new SharedViewModelFactory(getApplication(), previousNotes)).get(SharedViewModel.class);
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
                List<Note> remainingNotes = model.getRemainingNotes();
                PrefManager.init(ReviewActivity.this);
                PrefManager.setNotes(PREFS_REMAINING_NOTES, remainingNotes);
            }
        });

        /*
            SM2: Apply algorithm & Update the database
         */
        Scheduler scheduler = Scheduler.builder().build();
        Session session = model.getSession();
        scheduler.applySession(session);

        for (Note note : session.getNoteStatistics().keySet())
            model.update(note);

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