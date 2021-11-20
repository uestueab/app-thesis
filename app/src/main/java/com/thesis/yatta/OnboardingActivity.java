package com.thesis.yatta;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;

import com.thesis.yatta.databinding.ActivityOnboardingBinding;

import static com.thesis.yatta.constants.ConstantsHolder.PREFS_ONBOARDING_PASSED;

public class OnboardingActivity extends BaseActivity {

    private static final String TAG = "OnboardingActivity";
    private ActivityOnboardingBinding binding;

    TextView[] dots;
    OnboardingViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefManager.init(getApplicationContext());
        Boolean onboardingPassed = PrefManager.get(PREFS_ONBOARDING_PASSED, false);
        //move to starting screen, if starting screen was shown in the past
        if (onboardingPassed) {
            Intent startingScreenIntent = new Intent(OnboardingActivity.this, StartingScreenActivity.class);
            startActivity(startingScreenIntent);
            finish();
        }

        //remember that onboard-process was shown, don't show next time
        PrefManager.set(PREFS_ONBOARDING_PASSED, true);

        //get view references and inflate layout
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //make the screen as big as possible by hiding the action bar for now..
        getSupportActionBar().hide();

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItem(0) > 0) {
                    binding.slideViewPager.setCurrentItem(getItem(-1), true);
                }

            }
        });

        binding.nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getItem(0) < 3)
                    binding.slideViewPager.setCurrentItem(getItem(1), true);
                else {
                    Intent intent = new Intent(OnboardingActivity.this, StartingScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        binding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnboardingActivity.this, StartingScreenActivity.class);
                startActivity(intent);
                finish();

            }
        });

        viewPagerAdapter = new OnboardingViewPagerAdapter(this);
        binding.slideViewPager.setAdapter(viewPagerAdapter);

        setupIndicator(0);
        binding.slideViewPager.addOnPageChangeListener(viewLIstener);
    }

    public void setupIndicator(int position) {
        dots = new TextView[4];
        binding.indicatorLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive, getApplicationContext().getTheme()));
            binding.indicatorLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.active, getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewLIstener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setupIndicator(position);

            if (position > 0){
                binding.backbtn.setVisibility(View.VISIBLE);
                //Last slide should say 'DONE' instead of "NEXT"
                if (position ==  3)
                    binding.nextbtn.setText("DONE");
                else
                    binding.nextbtn.setText("NEXT");
            }
            else{
                binding.backbtn.setVisibility(View.INVISIBLE);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int item) {
        return binding.slideViewPager.getCurrentItem() + item;
    }
}