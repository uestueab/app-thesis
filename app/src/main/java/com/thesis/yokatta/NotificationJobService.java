package com.thesis.yokatta;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static com.thesis.yokatta.constants.ConstantsHolder.APP_CLOSED_AT;
import static com.thesis.yokatta.constants.ConstantsHolder.NOTIFY_DELAY_TIME;


public class NotificationJobService extends JobService {
    private static final String TAG = "NotificationJobService";
    private boolean jobCancelled = false;


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                PrefManager.init(getApplicationContext());
                /* Intention:
                    Make the notification appear after inactivity. (inactivity = closed time + x_days?)
                    Get the time the app was closed. (second parameter is to prohibit premature execution)
                 */
                Long app_closed_at = PrefManager.get(APP_CLOSED_AT, Long.MAX_VALUE);
                long notificationDelay = params.getExtras().getLong(NOTIFY_DELAY_TIME);

                if(System.currentTimeMillis() - notificationDelay > app_closed_at) {
                    Log.d(TAG, "JobService condition ist met");
                    Log.d(TAG, "Notification in progress...");

                    //Creating a notification channel
                    NotificationChannel channel = new NotificationChannel("channel1",
                            "hello",
                            NotificationManager.IMPORTANCE_HIGH);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel);

                    //Creating the notification object
                    NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "channel1");
                    //notification.setAutoCancel(true);
                    notification.setContentTitle("Hang in there!");
                    notification.setContentText("New reviews available.");
                    notification.setSmallIcon(R.drawable.ic_launcher_foreground);

                    //make the notification manager to issue a notification on the notification's channel
                    manager.notify(121, notification.build());
                    Log.d(TAG, "Notification send");

                    Log.d(TAG, "Job finished");
                    jobFinished(params, false);
                }else{
                    Log.d(TAG, "JobService condition not met");
                    Log.d(TAG, "Delay is set to: " + notificationDelay);
                }
            }
        }).start();


    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
