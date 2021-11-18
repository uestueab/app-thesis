package com.thesis.yatta.commander.commands;

import com.thesis.yatta.commander.Interface.ICommand;
import com.thesis.yatta.commander.receiver.PlayPronunciation;
import com.thesis.yatta.commander.receiver.ShowDiagram;

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
public class ShowDiagramCommand implements ICommand {

	ShowDiagram showDiagram;

	public ShowDiagramCommand() {
		this.showDiagram = new ShowDiagram();
	}

	@Override
	public void execute() {
		showDiagram.show();
	}

	@Override
	public <E> void setPref(E pref) {
		showDiagram.setPref(pref);
	}

	@Override
	public <E> void setState(E state) {
		showDiagram.setState(state);
	}
}
