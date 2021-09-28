package com.test.viewpagerfun.commander.commands;

import com.test.viewpagerfun.commander.Interface.ICommand;
import com.test.viewpagerfun.commander.receiver.HapticFeedback;
import com.test.viewpagerfun.commander.receiver.ShuffleCards;

/* HapticFeedbackCommand.class
 * ----------------------
 * Represents a command.
 * Each command has a execute() method, as agreed upon by the interface.
 * This method gets called by the commander.
 * Within the execute() method the methods of the encapsulated receiver objects are called.
 * 
 * Here the receiver is, HapticFeedback.
 * Furthermore a state is given to the receiver, so that more sophisticated logic can be implemented.
 */
public class HapticFeedbackCommand implements ICommand {

	HapticFeedback hapticFeedback;

	public HapticFeedbackCommand() {
		this.hapticFeedback = new HapticFeedback();
	}

	@Override
	public void execute() {
		hapticFeedback.vibrate();
	}

	@Override
	public <E> void setPref(E pref) {
		hapticFeedback.setPref(pref);
	}

	@Override
	public <E> void setState(E state) {
		hapticFeedback.setState(state);
	}


}
