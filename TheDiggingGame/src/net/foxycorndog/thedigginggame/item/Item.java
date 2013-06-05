package net.foxycorndog.thedigginggame.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.thedigginggame.TheDiggingGame;

/**
 * Class used to describe an Object that can be held in an Inventory
 * and have visual quality in the game.
 * 
 * @author	Braden Steffaniak
 * @since	Jun 2, 2013 at 9:11:11 PM
 * @since	v0.3
 * @version	Jun 2, 2013 at 9:11:11 PM
 * @version	v0.3
 */
public class Item
{
	private			int			x, y;
	private			int			cols, rows;
	private			int			stackSize;
	
	private			String		name;
	
	private	static	SpriteSheet	sprites;
	
	static
	{
		int cols = 16;
		int rows = 16;
		
		BufferedImage spriteSheet = null;
		
		try
		{
			spriteSheet = ImageIO.read(new File(TheDiggingGame.getResourcesLocation() + "res/images/texturepacks/16/minecraft/terrain.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		sprites = new SpriteSheet(spriteSheet, cols, rows);
	}
	
	/**
	 * Create an Item instance with the specific properties that guide
	 * how the Item interacts with its surroundings.
	 * 
	 * @param name The name of the Item that is being created.
	 * @param stackSize The amount of specific Item instances can be
	 * 		stacked in one Inventory Slot before filling the Slot to
	 * 		its max.
	 */
	public Item(String name, int x, int y, int cols, int rows, int stackSize)
	{
		this.name      = name;
		
		this.x         = x;
		this.y         = y;
		this.cols      = cols;
		this.rows      = rows;
		
		this.stackSize = stackSize;
	}
	
	/**
	 * @return The horizontal offset in the SpriteSheet.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * @return The vertical offset in the SpriteSheet.
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * @return The amount of columns the Tile takes up on the SpriteSheet.
	 */
	public int getCols()
	{
		return cols;
	}

	/**
	 * @return The amount of rows the Tile takes up on the SpriteSheet.
	 */
	public int getRows()
	{
		return rows;
	}
	
	/**
	 * Get whether the Item is able to be stacked in an Inventory.
	 * 
	 * @return Whether the Item is able to be stacked in an Inventory.
	 */
	public boolean isStackable()
	{
		return stackSize > 1;
	}
	
	/**
	 * Get the amount of specific Item instances can be stacked in one
	 * Inventory Slot before filling the Slot to its max.
	 * 
	 * @return The amount of specific Item instances can be stacked in one
	 * 		Inventory Slot before filling the Slot to its max.
	 */
	public int getStackSize()
	{
		return stackSize;
	}
	
	/**
	 * Get the name of the Item.
	 * 
	 * @return The name of the Item.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get a String representation of the Item instance. In this case it
	 * is the name of the Item.
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * @return The name of the Item.
	 */
	public String toString()
	{
		return name;
	}
	
	/**
	 * Get the SpriteSheet used for rendering the Items.
	 * 
	 * @return The SpriteSheet used for rendering the Items.
	 */
	public static SpriteSheet getSprites()
	{
		return sprites;
	}
}