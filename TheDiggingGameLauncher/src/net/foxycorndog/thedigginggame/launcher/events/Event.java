package net.foxycorndog.thedigginggame.launcher.events;

import net.foxycorndog.jfoxylib.components.Button;

/**
 * Base class for all of the other Events. Holds the information for the
 * time that the Event happened.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 10, 2013 at 3:28:46 PM
 * @since	v0.2
 * @version Mar 10, 2013 at 3:28:46 PM
 * @version	v0.2
 */
public class Event
{
	private long	when;
	
	/**
	 * Create an Event.
	 */
	public Event()
	{
		when         = System.currentTimeMillis();
	}
	
	/**
	 * Get when the event occurred.
	 * 
	 * @return
	 */
	public long when()
	{
		return when;
	}
}