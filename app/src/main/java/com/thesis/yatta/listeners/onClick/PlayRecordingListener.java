package com.thesis.yatta.listeners.onClick;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.View;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayRecordingListener implements View.OnClickListener {

    private Context context;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private String flashCardPronunciation;
    private boolean recordingExists;

    @Override
    public void onClick(View v) {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();

        // if there was a recording...
        if (mediaRecorder == null && recordingExists) {
            if (!mediaPlayer.isPlaying()) {
                try {
                    //when a flashCard already had a pronunciation recording.. play that
                    //or else play the
                    mediaPlayer.setDataSource(flashCardPronunciation);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    mediaPlayer = null;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
