package com.test.viewpagerfun.commander.receiver;


/* PlaySound.class
 * ---------------
 * The class who's methods get called by the Command 0bject.
 */
public class ShuffleCards {
	private static String pref_value = null;
	

	public void shuffle() {
		// Since we have a state (field: preference), we can have a more sophisticated method
	}

	
	public <E> void setState(E pref) {
		if(pref_value == null)
			pref_value = (String) pref;
	}


}
