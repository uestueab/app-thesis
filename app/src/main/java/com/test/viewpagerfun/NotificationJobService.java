package com.test.viewpagerfun;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.core.app.NotificationCompat;


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
                int notesCount = params.getExtras().getInt("count");

                //Creating a notification channel
                NotificationChannel channel=new NotificationChannel("channel1",
                        "hello",
                        NotificationManager.IMPORTANCE_HIGH);
                NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);

                //Creating the notification object
                NotificationCompat.Builder notification=new NotificationCompat.Builder(getApplicationContext(),"channel1");
                //notification.setAutoCancel(true);
                notification.setContentTitle("Yatta says:");
                notification.setContentText("You have currently " + notesCount + " review items");
                notification.setSmallIcon(R.drawable.ic_launcher_foreground);

                //make the notification manager to issue a notification on the notification's channel
                manager.notify(121,notification.build());

                Log.d(TAG, "run: available review: " + notesCount);


                Log.d(TAG, "Job finished");
                jobFinished(params, false);
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
