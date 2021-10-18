package com.thesis.yatta.listeners.onClick;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.View;
import android.widget.Toast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StopRecordingListener implements View.OnClickListener {

    private Context context;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean recordingExists;

    @Override
    public void onClick(View v) {
        // if there is a recording going on...
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();

            mediaRecorder = null;
            recordingExists = true;

            Toast.makeText(getContext(), "Recording stopped...", Toast.LENGTH_SHORT).show();
        }

    }
}
