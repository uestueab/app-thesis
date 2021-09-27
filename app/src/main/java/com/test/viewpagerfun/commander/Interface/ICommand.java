package com.test.viewpagerfun.commander.Interface;

/* ICommander
* -----------------
* Makes sure each command implements these methods
*/

public interface ICommand {
	public void execute();
	public <E> void setState(E state);
}
