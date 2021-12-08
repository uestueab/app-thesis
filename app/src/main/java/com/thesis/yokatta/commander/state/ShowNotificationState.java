package com.thesis.yokatta.commander.state;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.thesis.yokatta.commander.receiver.ShowNotification;

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