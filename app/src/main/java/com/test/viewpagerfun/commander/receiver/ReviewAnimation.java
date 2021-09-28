package com.test.viewpagerfun.commander.receiver;


import android.util.Log;

import com.test.viewpagerfun.PrefManager;
import com.test.viewpagerfun.R;
import com.test.viewpagerfun.commander.state.ReviewAnimationState;
import com.test.viewpagerfun.model.entity.Note;

import java.util.Collections;
import java.util.List;

/* ReviewAnimation.class
 * ---------------
 * The class who's methods get called by the Command object.
 */
public class ReviewAnimation {
	private static String pref_value = null;
	private static ReviewAnimationState state = null;


	// Since we have a state and the pref key, we can have a more sophisticated method
	public void playAnimation() {
		if(state == null)
		return;

		boolean animation_enabled = PrefManager.get(pref_value,false);
		if(animation_enabled){
			if (state.isHasFailed())
				state.getBinding().reviewResultAnimation.setAnimation(R.raw.wrong);
			else
				state.getBinding().reviewResultAnimation.setAnimation(R.raw.correct);
			state.getBinding().reviewResultAnimation.playAnimation();
		}
	}

	public <E> void setState(E pref) {
			state = (ReviewAnimationState) pref;
	}

	public <E> void setPref(E pref) {
			pref_value = (String) pref;
	}


}
