package com.test.viewpagerfun.commander.commands;

import com.test.viewpagerfun.commander.Interface.ICommand;
import com.test.viewpagerfun.commander.receiver.PlaySound;

/* PlaySoundCommand.class
 * ----------------------
 * Represents a command.
 * Each command has a execute() method, as agreed upon by the interface.
 * This method gets called by the commander.
 * Within the execute() method the methods of the encapsulated receiver objects are called.
 * 
 * Here the receiver is, PlaySound.
 * Furthermore a state is given to the receiver, so that more sophisticated logic can be implemented.
 */
public class PlaySoundCommand implements ICommand {
	
	PlaySound playSound;
	
	public PlaySoundCommand() {
		this.playSound = new PlaySound();
	}

	@Override
	public void execute() {
		playSound.play();
	}

	@Override
	public <E> void setState(E state) {
		playSound.setState(state);
		
	}
	

}
