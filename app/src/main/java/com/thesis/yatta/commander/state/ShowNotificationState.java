package com.thesis.yatta.commander.state;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.thesis.yatta.commander.receiver.ShowNotification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowNotificationState {
    private Context context;

    public ShowNotificationState(Context context){
        this.context = context;
    }
}