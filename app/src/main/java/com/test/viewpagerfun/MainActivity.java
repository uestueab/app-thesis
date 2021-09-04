package com.test.viewpagerfun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.bluetooth.le.ScanSettings;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    //number of fragments
    private static final int NUM_PAGES = 2;

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
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void nextFragment () {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

//    public void previous_fragment() {
//        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
//    }


    /**
     * A simple pager adapter that represents 2 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new FragmentScreenSlidePage();
                    break;
                case 1:
                    fragment = new FragmentScreenSlidePageTwo();
                    break;
            }
            assert fragment != null;
            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}