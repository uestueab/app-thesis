package com.test.viewpagerfun.commander.receiver;


import com.test.viewpagerfun.PrefManager;
import com.test.viewpagerfun.model.entity.FlashCard;

import java.util.Collections;
import java.util.List;

/* ShuffleCards.class
 * ---------------
 * The class who's methods get called by the Command object.
 */
public class ShuffleCards {
	private static String pref_value = null;
	private static List<FlashCard> state = null;


	// Since we have a state and the pref key, we can have a more sophisticated method
	public void shuffle() {
		if(state == null)
			return;

		boolean shuffle_enabled = PrefManager.get(pref_value,false);
		if(shuffle_enabled)
			Collections.shuffle(state);
	}


	public <E> void setState(E pref) {
			state = (List<FlashCard>) pref;
	}

	public <E> void setPref(E pref) {
			pref_value = (String) pref;
	}


}
