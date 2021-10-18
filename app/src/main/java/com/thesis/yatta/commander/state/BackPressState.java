package com.thesis.yatta.commander.state;

import android.app.Activity;
import android.widget.Toast;

import com.thesis.yatta.databinding.ReviewDetailedResultFragmentBinding;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackPressState {

    private Activity activity;
    private long backPressedTime;
    private final Toast toast;

    public BackPressState(Activity activity) {
        this.activity = activity;
        this.backPressedTime = 0;
        this.toast = Toast.makeText(activity, "Press back again to pause review", Toast.LENGTH_SHORT);
    }

}