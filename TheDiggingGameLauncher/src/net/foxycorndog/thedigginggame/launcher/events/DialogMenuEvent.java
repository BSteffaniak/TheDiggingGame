package net.foxycorndog.thedigginggame.launcher.events;

import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.thedigginggame.launcher.menu.DialogMenu;

/**
 * Class that holds information from a notification of a
 * DialogMenuListener. Tells whether the Button pressed was yes or no.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 14, 2013 at 7:40:53 PM
 * @since	v0.1
 * @version Mar 14, 2013 at 7:40:53 PM
 * @version	v0.1
 */
public class DialogMenuEvent extends Event
{
	private boolean		yes;
	
	private DialogMenu	source;
	
	/**
	 * Create a DialogMenuEvent with the specified value of yes.
	 * 
	 * @param yes Whether the Button pressed was "yes". If not, it
	 * 		was "no".
	 */
	public DialogMenuEvent(boolean yes, DialogMenu source)
	{
		this.yes    = yes;
		
		this.source = source;
	}
	
	/**
	 * Return whether the Button pressed was "yes". If not, it
	 * was "no".
	 * 
	 * @return Whether the Button pressed was "yes". If not, it
	 * 		was "no".
	 */
	public boolean wasYes()
	{
		return yes;
	}
	
	/**
	 * Get the source of the Event.
	 * 
	 * @return The DialogMenu that caused the event.
	 */
	public DialogMenu getSource()
	{
		return source;
	}
}