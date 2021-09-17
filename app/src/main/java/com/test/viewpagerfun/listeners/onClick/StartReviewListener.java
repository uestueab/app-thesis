package com.test.viewpagerfun.listeners.onClick;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import com.test.viewpagerfun.ReviewActivity;
import com.test.viewpagerfun.StartingScreenActivity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StartReviewListener implements View.OnClickListener {

    private Context currentActivity;
    private Class targetActivity;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getCurrentActivity(), getTargetActivity());
        resultLauncher.launch(intent);
    }
}
