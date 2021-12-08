package com.thesis.yokatta.commander.commands;

import com.thesis.yokatta.commander.Interface.ICommand;
import com.thesis.yokatta.commander.receiver.BackPress;
import com.thesis.yokatta.commander.receiver.PlayPronunciation;

/* PlayPronunciationCommand.class
 * ----------------------
 * Represents a command.
 * Each command has a execute() method, as agreed upon by the interface.
 * This method gets called by the commander.
 * Within the execute() method the methods of the encapsulated receiver objects are called.
 * 
 * Here the receiver is, PlayPronunciation.
 * Furthermore a state is given to the receiver, so that more sophisticated logic can be implemented.
 */
public class PlayPronunciationCommand implements ICommand {

	PlayPronunciation playPronunciation;

	public PlayPronunciationCommand() {
		this.playPronunciation = new PlayPronunciation();
	}

	@Override
	public void execute() {
		playPronunciation.play();
	}

	@Override
	public <E> void setPref(E pref) {
		playPronunciation.setPref(pref);
	}

	@Override
	public <E> void setState(E state) {
		playPronunciation.setState(state);
	}

}
