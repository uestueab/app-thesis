package com.test.viewpagerfun.commander.Interface;

/* ICommander
* -----------------
* Makes sure each command implements these methods
*/

public interface ICommand {
	public void execute();
	public <E> void setPref(E pref);

	//give each command the chance compute a more sophisticated task
	public <E> void setState(E state);
}
