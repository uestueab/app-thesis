package com.test.viewpagerfun.commander.commands;

import com.test.viewpagerfun.commander.Interface.ICommand;
import com.test.viewpagerfun.commander.receiver.ShuffleCards;

/* ShuffleCardsCommand.class
 * ----------------------
 * Represents a command.
 * Each command has a execute() method, as agreed upon by the interface.
 * This method gets called by the commander.
 * Within the execute() method the methods of the encapsulated receiver objects are called.
 * 
 * Here the receiver is, ShuffleCards.
 * Furthermore a state is given to the receiver, so that more sophisticated logic can be implemented.
 */
public class ShuffleCardsCommand implements ICommand {

	ShuffleCards shuffleCards;

	public ShuffleCardsCommand() {
		this.shuffleCards = new ShuffleCards();
	}

	@Override
	public void execute() {
		shuffleCards.shuffle();
	}

	@Override
	public <E> void setPref(E pref) {
		shuffleCards.setPref(pref);
	}

	@Override
	public <E> void setState(E state) {
		shuffleCards.setState(state);
	}


}
