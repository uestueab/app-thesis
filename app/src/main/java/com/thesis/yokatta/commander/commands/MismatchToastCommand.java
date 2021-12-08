package com.thesis.yokatta.commander.commands;

import com.thesis.yokatta.commander.Interface.ICommand;
import com.thesis.yokatta.commander.receiver.MismatchToast;

/* MismatchToastCommand.class
 * ----------------------
 * Represents a command.
 * Each command has a execute() method, as agreed upon by the interface.
 * This method gets called by the commander.
 * Within the execute() method the methods of the encapsulated receiver objects are called.
 * 
 * Here the receiver is, MismatchToast.
 * Furthermore a state is given to the receiver, so that more sophisticated logic can be implemented.
 */
public class MismatchToastCommand implements ICommand {

	MismatchToast mismatchToast;

	public MismatchToastCommand() {
		this.mismatchToast = new MismatchToast();
	}

	@Override
	public void execute() {
		mismatchToast.show();
	}

	@Override
	public <E> void setPref(E pref) {
		mismatchToast.setPref(pref);
	}

	@Override
	public <E> void setState(E state) {
		mismatchToast.setState(state);
	}


}
