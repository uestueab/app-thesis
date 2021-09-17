package com.test.viewpagerfun;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// pager adapter that represents NUM_PAGES ScreenSlidePageFragment objects, in sequence.
public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    //number of fragments
    private static final int NUM_PAGES = 2;

    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ReviewInputFragment();
                break;
            case 1:
                fragment = new ReviewDetailedResultFragment();
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
