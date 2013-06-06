package net.foxycorndog.thedigginggame.chat;

import net.foxycorndog.thedigginggame.actor.Actor;
import net.foxycorndog.thedigginggame.item.tile.Tile;

/**
 * Class used to run Commands.
 * 
 * @author	Braden Steffaniak
 * @since	Jun 5, 2013 at 10:14:18 PM
 * @since	v0.3
 * @version	Jun 5, 2013 at 10:14:18 PM
 * @version	v0.3
 */
public class Command
{
	/**
	 * Run a specific Command and generate a response.
	 * 
	 * @param command The Command to run.
	 * @return The response to the running of the Command. A null
	 * 		response means that the Command ran properly.
	 */
	public static String run(String command, Actor actor)
	{
		String response = null;
		
		String split[] = command.toLowerCase().split(" ");
		
		int index = 0;
		
		try
		{
			if (split[index++].equals("give"))
			{
				String itemName = "";
				
				while (index < split.length - 1)
				{
					itemName += split[index++];
					
					if (index < split.length - 1)
					{
						itemName += " ";
					}
				}
				
				String num      = null;
				
				int    quantity = 0;
				
				try
				{
					num      = split[index++];
					
					quantity = Integer.valueOf(num);
				}
				catch (NumberFormatException e)
				{
					return "Expected number, instead received: '" + num + "'";
				}
				
				Tile tile = Tile.getTile(itemName);
				
				actor.getInventory().addItem(tile, quantity);
				
				return null;
			}
			
			index = 0;
			
			if (split[index++].equals("remove"))
			{
				String itemName = "";
				
				while (index < split.length - 1)
				{
					itemName += split[index++];
					
					if (index < split.length - 1)
					{
						itemName += " ";
					}
				}
				
				String num      = null;
				
				int    quantity = 0;
				
				try
				{
					num      = split[index++];
					
					quantity = Integer.valueOf(num);
				}
				catch (NumberFormatException e)
				{
					return "Expected number, instead received: '" + num + "'";
				}
				
				Tile tile = Tile.getTile(itemName);
				
				actor.getInventory().removeItem(tile, quantity);
				
				return null;
			}
			
			response = "Unknown command.";
		}
		catch (IndexOutOfBoundsException e)
		{
			response = "Unknown command.";
		}
		
		return response;
	}
}