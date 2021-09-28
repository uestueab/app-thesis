package com.test.viewpagerfun.commander.state;

import android.app.Activity;
import android.widget.Toast;

import com.test.viewpagerfun.StartingScreenActivity;
import com.test.viewpagerfun.commander.receiver.BackPress;
import com.test.viewpagerfun.databinding.ReviewDetailedResultFragmentBinding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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