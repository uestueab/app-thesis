package com.test.viewpagerfun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.test.viewpagerfun.model.entity.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewActivity extends FragmentActivity {

    public static final String EXTRA_REMAINING_REVIEWS = "extra_remaining_reviews";

    // make use of animations, when moving to next fragment
    private ViewPager2 viewPager;

    private SharedViewModel model;

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


        model = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    public void nextFragment () { viewPager.setCurrentItem(viewPager.getCurrentItem()+1); }
    public void previous_fragment() { viewPager.setCurrentItem(viewPager.getCurrentItem()-1,false); }


    @Override
    public void onBackPressed() {
            Intent intent = new Intent();
            List<Note> remainingNotes = model.getRemainingNotes();

            /* Check if the back button was pressed on a fragment other than the review input fragment.
             * That means the review item has lapsed/passed! So remove it from the list.
             */
            if(viewPager.getCurrentItem() != 0)
                remainingNotes.remove(0);
            intent.putExtra(EXTRA_REMAINING_REVIEWS, remainingNotes.size());
            setResult(RESULT_OK,intent);
            finish();
    }
}