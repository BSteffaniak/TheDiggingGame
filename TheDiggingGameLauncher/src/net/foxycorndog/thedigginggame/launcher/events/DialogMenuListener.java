package net.foxycorndog.thedigginggame.launcher.events;

/**
 * Class that is used to listen for when a Button is pressed in a
 * DialogMenu.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 14, 2013 at 7:43:17 PM
 * @since	v0.1
 * @version Mar 14, 2013 at 7:43:17 PM
 * @version	v0.1
 */
public interface DialogMenuListener
{
	/**
	 * Called when a Button has been pressed in the containing
	 * DialogMenuListener.
	 * 
	 * @param event The DialogMenuEvent that was sent with this press.
	 */
	public abstract void buttonPressed(DialogMenuEvent event);
}