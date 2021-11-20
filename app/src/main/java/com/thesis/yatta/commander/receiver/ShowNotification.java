package com.thesis.yatta.commander.receiver;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import com.thesis.yatta.NotificationJobService;
import com.thesis.yatta.PrefManager;
import com.thesis.yatta.StartingScreenActivity;
import com.thesis.yatta.commander.state.BackPressState;
import com.thesis.yatta.commander.state.ShowNotificationState;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static com.thesis.yatta.constants.ConstantsHolder.NOTIFY_DEFAULT_DELAY_TIME;
import static com.thesis.yatta.constants.ConstantsHolder.NOTIFY_DELAY_TIME;

/* ShowNotification.class
 * ---------------
 * The class who's methods get called by the Command object.
 */
public class ShowNotification {
    private static String pref_value = null;
    private ShowNotificationState state = null;


    // Since we have a state and the pref key, we can have a more sophisticated method
    public void show() {
        //if(state == null)
        //	return;

        boolean notification_enabled = PrefManager.get(pref_value, false);
        if (notification_enabled) {
            PersistableBundle bundle = new PersistableBundle();
            bundle.putLong(NOTIFY_DELAY_TIME, NOTIFY_DEFAULT_DELAY_TIME);

            ComponentName componentName = new ComponentName(state.getContext(), NotificationJobService.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setExtras(bundle)
                    .setPersisted(true)
                    .setPeriodic(NOTIFY_DEFAULT_DELAY_TIME)
                    .build();

            JobScheduler scheduler = (JobScheduler) state.getContext().getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(getClass().getSimpleName(), "Job scheduled");
            } else {
                Log.d(getClass().getSimpleName(), "Job scheduling failed");
            }
        }
    }

    public <E> void setState(E pref) {
        if (state == null)
            state = (ShowNotificationState) pref;
    }

    public <E> void setPref(E pref) {
        if (pref_value == null)
            pref_value = (String) pref;
    }

    private void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) state.getContext().getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(getClass().getSimpleName(), "Job cancelled");
    }

}
