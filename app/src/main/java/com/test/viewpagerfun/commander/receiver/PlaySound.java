package com.test.viewpagerfun.commander.receiver;


/* PlaySound.class
 * ---------------
 * The class who's methods get called by the Command 0bject.
 */
public class PlaySound {
	private static String preference = null;
	

	public void play() {
		// Since we have a state (field: preference), we can have a more sophisticated method
		if(preference.equals("PLAY_PREF"))
			System.out.println("[Playing Sound]: I'm blue dabadi...");
		else 
			System.out.println("[Playing Sound]: Do you believe in love...");
	}

	
	public <E> void setState(E pref) {
		if(preference == null)
			preference = (String) pref;
	}


	@Override
	public String toString() {
		return "PlaySound = [preference: " + preference + "]" ;
	}
	
	

}
