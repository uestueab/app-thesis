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

    // make use of animations, when moving to next fragment
    private ViewPager2 viewPager;
    // The pager adapter, which provides the pages to the view pager widget.
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        // use button navigation, instead of gesture swiping to next fragment
        viewPager.setUserInputEnabled(false);

        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);


     SharedViewModel model = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    public void nextFragment () { viewPager.setCurrentItem(viewPager.getCurrentItem()+1); }
    public void previous_fragment() { viewPager.setCurrentItem(viewPager.getCurrentItem()-1,false); }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            /* The line below makes it possible for the user to re-enter the review!
             * This is not what we want. Rather make an intent to go back to the starting screen..
             *
             *  viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
             */
            Intent intent = new Intent(this,StartingScreenActivity.class);
            startActivity(intent);
        }
    }

}