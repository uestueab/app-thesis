package com.thesis.yatta.commander.commands;

import com.thesis.yatta.commander.Interface.ICommand;
import com.thesis.yatta.commander.receiver.BackPress;
import com.thesis.yatta.commander.receiver.ShowNotification;

/* BackPressCommand.class
 * ----------------------
 * Represents a command.
 * Each command has a execute() method, as agreed upon by the interface.
 * This method gets called by the commander.
 * Within the execute() method the methods of the encapsulated receiver objects are called.
 * 
 * Here the receiver is, BackPress.
 * Furthermore a state is given to the receiver, so that more sophisticated logic can be implemented.
 */
public class ShowNotificationCommand implements ICommand {

	ShowNotification showNotification;

	public ShowNotificationCommand() {
		this.showNotification = new ShowNotification();
	}

	@Override
	public void execute() {
		showNotification.show();
	}

	@Override
	public <E> void setPref(E pref) {
		showNotification.setPref(pref);
	}

	@Override
	public <E> void setState(E state) {
		showNotification.setState(state);
	}


}
