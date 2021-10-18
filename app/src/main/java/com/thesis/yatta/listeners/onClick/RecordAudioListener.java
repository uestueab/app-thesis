package com.thesis.yatta.listeners.onClick;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordAudioListener implements View.OnClickListener {

    private Context context;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private String flashCardPronunciation;
    private String recordingFilePath;

    @Override
    public void onClick(View v) {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();

            try {
                flashCardPronunciation = getRecordingFilePath();

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.OGG);
                mediaRecorder.setOutputFile(flashCardPronunciation);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);
                mediaRecorder.setAudioEncodingBitRate(128000);
                mediaRecorder.setAudioSamplingRate(44100);
                mediaRecorder.prepare();
                mediaRecorder.start();

                Toast.makeText(getContext(), "Recording started...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
