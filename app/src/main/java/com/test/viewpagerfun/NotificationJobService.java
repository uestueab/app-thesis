package com.test.viewpagerfun;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static com.test.viewpagerfun.constants.ConstantsHolder.NOTIFICATIONS_LAST_RUN;


public class NotificationJobService extends JobService {
    private static final String TAG = "NotificationJobService";
    private boolean jobCancelled = false;

    private static final int delay = 15 * 60 * 1000;

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
                long last_app_session = PrefManager.get(NOTIFICATIONS_LAST_RUN, Long.MAX_VALUE);

                if(System.currentTimeMillis() - delay > last_app_session) {
                    int notesCount = params.getExtras().getInt("count");

                    //Creating a notification channel
                    NotificationChannel channel = new NotificationChannel("channel1",
                            "hello",
                            NotificationManager.IMPORTANCE_HIGH);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel);

                    //Creating the notification object
                    NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "channel1");
                    //notification.setAutoCancel(true);
                    notification.setContentTitle("New Reviews available");
                    notification.setContentText(notesCount + " items are available");
                    notification.setSmallIcon(R.drawable.ic_launcher_foreground);

                    //make the notification manager to issue a notification on the notification's channel
                    manager.notify(121, notification.build());

                    Log.d(TAG, "run: available review: " + notesCount);


                    Log.d(TAG, "Job finished");
                    jobFinished(params, false);
                }else{
                    Log.d(TAG, "JobService condition not met");
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
