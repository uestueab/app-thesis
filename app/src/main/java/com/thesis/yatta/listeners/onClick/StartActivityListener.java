package com.thesis.yatta.listeners.onClick;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StartActivityListener implements View.OnClickListener {

    private Context currentActivity;
    private Class targetActivity;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getCurrentActivity(), getTargetActivity());
        currentActivity.startActivity(intent);
    }
}
