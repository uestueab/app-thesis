package com.thesis.yokatta.commander.receiver;


import android.app.Activity;
import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.thesis.yokatta.PrefManager;

import static android.content.Context.VIBRATOR_SERVICE;

/* ShuffleCards.class
 * ---------------
 * The class who's methods get called by the Command object.
 */
public class HapticFeedback {
	private static String pref_value = null;
	private Activity state = null;


	// Since we have a state and the pref key, we can have a more sophisticated method
	public void vibrate() {
		if(state == null)
			return;

		boolean haptic_feedback_enabled = PrefManager.get(pref_value,false);

		//Vibrate phone if set in settings
		if(haptic_feedback_enabled){
			Vibrator v = (Vibrator) state.getSystemService(VIBRATOR_SERVICE);
			AudioAttributes audioAttributes = new AudioAttributes.Builder()
					.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
					.setUsage(AudioAttributes.USAGE_ALARM)
					.build();
			VibrationEffect ve = VibrationEffect.createOneShot(100,
					VibrationEffect.DEFAULT_AMPLITUDE);
			v.vibrate(ve, audioAttributes);
		}
	}


	public <E> void setState(E pref) {
		if(state == null)
			state = (Activity) pref;
	}
	public <E> void setPref(E pref) {
		if(pref_value == null)
			pref_value = (String) pref;
	}


}
