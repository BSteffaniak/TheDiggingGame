package net.foxycorndog.thedigginggame;

/**
 * Interface used for the launcher to have control of certain methods
 * from the game.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 11, 2013 at 3:45:29 PM
 * @since	v0.1
 * @version Mar 11, 2013 at 3:45:29 PM
 * @version	v0.1
 */
public interface GameInterface
{	
	/**
	 * Start the game. Exits the current menu that it is in.
	 */
	public void startGame();

	/**
	 * Initialize the data for the game.
	 * 
	 * @param online Whether or not to include online capabilities in the game.
	 * @param resourcesLocation The location to the game parent folder in which
	 * 		the res folder is located. All of the games resources are located
	 * 		in the res folder.
	 */
	public void init(boolean online, String resourcesLocation);

	/**
	 * Method that renders using the Ortho method.
	 */
	public void render2D();

	/**
	 * Method that renders in the 3D mode.
	 */
	public void render3D();

	/**
	 * Method that is called each time before the render methods.
	 */
	public void loop();
	
	/**
	 * Get the current version of the client that is being ran.
	 * 
	 * @return The current version in the String format.
	 */
	public String getVersion();
}