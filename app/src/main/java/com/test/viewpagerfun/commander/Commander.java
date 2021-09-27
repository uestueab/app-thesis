package com.test.viewpagerfun.commander;

import com.test.viewpagerfun.commander.Interface.ICommand;

import java.util.HashMap;

/* Commander.class
 * ---------------
 * Works like a remote control, where each button corresponds to a command.
 * The commander knows nothing about the implementation of a certain command.
 * It only calls the execute method of the command it is mapped to.
 */
public class Commander {
	
	private static HashMap<Object, ICommand> commandMap;

	private Commander(){}

	public static void init() {
		if (commandMap == null) {
			commandMap = new HashMap<Object, ICommand>();
		}
	}
	
	public static <E> void setCommand(E pref, ICommand command) {
		command.setPref(pref);
		commandMap.put(pref, command);
	}

	public static <E> void setState(E pref, E state) {
		if(commandMap.containsKey(pref))
			commandMap.get(pref).setState(state);
		else
			throw new RuntimeException("Specified key doesn't exist");
	}


	public static <E> void run(E pref) {
		if(commandMap.containsKey(pref)) 
			commandMap.get(pref).execute();
		else throw new RuntimeException("Specified key doesn't exist");
	}
	
	public static int commandCount() { return commandMap.size();}


}
