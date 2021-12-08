package com.thesis.yokatta.commander.receiver;


import android.media.MediaPlayer;
import android.os.Environment;

import com.thesis.yokatta.PrefManager;
import com.thesis.yokatta.commander.state.PlayPronunciationState;

import java.io.File;
import java.io.IOException;

/* PlayPronunciation.class
 * ---------------
 * The class who's methods get called by the Command object.
 */
public class PlayPronunciation {
	private static String pref_value = null;
	private PlayPronunciationState state = null;


	// Since we have a state and the pref key, we can have a more sophisticated method
	public void play() {
		if(state == null)
			return;

		boolean sound_enabled = PrefManager.get(pref_value,true);
		if(sound_enabled){
			if(state.getFlashCard().getPronunciation() != null){
				MediaPlayer mediaPlayer = new MediaPlayer();
				File fPronunciation = new File(state.getContextWrapper().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
						state.getFlashCard().getPronunciation());

				try {
					mediaPlayer.setDataSource(fPronunciation.getPath());
					mediaPlayer.prepare();
					mediaPlayer.start();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public <E> void setState(E pref) {
		if(state == null)
			state = (PlayPronunciationState) pref;
	}
	public <E> void setPref(E pref) {
		if(pref_value == null)
			pref_value = (String) pref;
	}

}
