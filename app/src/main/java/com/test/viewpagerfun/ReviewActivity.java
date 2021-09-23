package com.test.viewpagerfun;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.test.viewpagerfun.model.entity.Note;
import com.test.viewpagerfun.sm2.Scheduler;
import com.test.viewpagerfun.sm2.Session;
import com.test.viewpagerfun.viewmodel.SharedViewModel;
import com.test.viewpagerfun.viewmodel.SharedViewModelFactory;

import java.io.Serializable;
import java.util.List;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

public class ReviewActivity extends FragmentActivity {

    private final String TAG = this.getClass().getSimpleName();

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

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        // use button navigation, instead of gesture swiping to next fragment
        viewPager.setUserInputEnabled(false);

        // The pager adapter, which provides the pages to the view pager widget.
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        resumeReview();
    }

    private void resumeReview() {
        List<Note> previousNotes = new PrefManager<Note>(getApplicationContext())
                .getNotes(PREFS_REMAINING_NOTES);

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

        Log.d(TAG, "onBackPressed: ");
        /*  Avoid accidentally going out of review by hitting the back button.
            Instead leave review only when back button was pressed in quick succession.
         */
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(this, StartingScreenActivity.class);


            /* Check if the back button was pressed on a fragment other than the review input fragment.
             * That means the review item has lapsed/passed! So remove it from the list.
             */
//            if (viewPager.getCurrentItem() != 0)
//                remainingNotes.remove(0);

//            Bundle bundle = new Bundle();
//            bundle.putSerializable(BUNDLE_REMAINING_NOTES, (Serializable) remainingNotes);
//
//            intent.putExtra(EXTRA_REMAINING_REVIEWS, bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            toast = Toast.makeText(this, "Press back again to pause review", Toast.LENGTH_SHORT);
            toast.show();
        }

        backPressedTime = System.currentTimeMillis();
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

        Log.d(TAG, "onPause: ");

//        Scheduler scheduler = Scheduler.builder().build();
//        Session session = model.getSession();
//        scheduler.applySession(session);
//
//        for (Note note : session.getNoteStatistics().keySet())
//            model.update(note);

        List<Note> remainingNotes = model.getRemainingNotes();

//        if (viewPager.getCurrentItem() != 0)
//            remainingNotes.remove(0);

        new PrefManager<>(getApplicationContext()).setNotes(PREFS_REMAINING_NOTES, remainingNotes);

        if(toast != null)
            toast.cancel();
    }
}