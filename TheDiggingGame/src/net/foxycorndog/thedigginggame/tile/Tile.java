package net.foxycorndog.thedigginggame.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.thedigginggame.TheDiggingGame;

/**
 * Class that holds information for a Tile that is used in the terrain.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 22, 2013 at 4:23:23 AM
 * @since	v0.2
 * @version Feb 22, 2013 at 4:23:24 AM
 * @version	v0.2
 */
public class Tile
{
	private			boolean					collidable;
	
	private			int						x, y;
	private			int						cols, rows;
	
	private			float					transparency;
	private			float					light;
	
	private	static	int						tileSize;
	
	private	static	SpriteSheet				terrainSprites;
	
	private	static	HashMap<String, Tile>	tiles;
	
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
		
		terrainSprites = new SpriteSheet(spriteSheet, cols, rows);
		
		tileSize = 16;
		
		tiles = new HashMap<String, Tile>();
		
		int index = 0;
		
		tiles.put("Stone",				new Tile(1,  0, 1, 1, 0, 0, true));
		tiles.put("Dirt",				new Tile(2,  0, 1, 1, 0, 0, true));
		tiles.put("Grass",				new Tile(3,  0, 1, 1, 0, 0, true));
		tiles.put("Wooden Planks",		new Tile(4,  0, 1, 1, 0, 0, true));
		tiles.put("Double Stone Slab",	new Tile(5,  0, 1, 1, 0, 0, true));
		tiles.put("Stone Slab",			new Tile(6,  0, 1, 1, 0, 0, true));
		tiles.put("Bricks",				new Tile(7,  0, 1, 1, 0, 0, true));
		tiles.put("TNT",				new Tile(8,  0, 1, 1, 0, 0, true));
		tiles.put("Cobblestone",		new Tile(0,  1, 1, 1, 0, 0, true));
		tiles.put("Bedrock",			new Tile(1,  1, 1, 1, 0, 0, true));
		tiles.put("Sand",				new Tile(2,  1, 1, 1, 0, 0, true));
		tiles.put("Gravel",				new Tile(3,  1, 1, 1, 0, 0, true));
		tiles.put("Log", 				new Tile(4,  1, 1, 1, 0, 0, true));
		tiles.put("Iron Block",			new Tile(6,  1, 1, 1, 0, 0, true));
		tiles.put("Gold Block",			new Tile(7,  1, 1, 1, 0, 0, true));
		tiles.put("Diamond Block",		new Tile(8,  1, 1, 1, 0, 0, true));
		tiles.put("Emerald Block",		new Tile(9,  1, 1, 1, 0, 0, true));
		tiles.put("Gold Ore",			new Tile(0,  2, 1, 1, 0, 0, true));
		tiles.put("Iron Ore",			new Tile(1,  2, 1, 1, 0, 0, true));
		tiles.put("Coal Ore",			new Tile(2,  2, 1, 1, 0, 0, true));
		tiles.put("Bookshelf",			new Tile(3,  2, 1, 1, 0, 0, true));
		tiles.put("Mossy Cobblestone",	new Tile(4,  2, 1, 1, 0, 0, true));
		tiles.put("Obsidian",			new Tile(5,  2, 1, 1, 0, 0, true));
		tiles.put("Furnace",			new Tile(12, 2, 1, 1, 0, 0, true));
		tiles.put("Dispenser",			new Tile(14, 2, 1, 1, 0, 0, true));
		tiles.put("Sponge",				new Tile(0,  3, 1, 1, 0, 0, true));
		tiles.put("Glass",				new Tile(1,  3, 1, 1, 0.05f, 0, true));
		tiles.put("Diamond Ore",		new Tile(2,  3, 1, 1, 0, 0, true));
		tiles.put("Redstone Ore",		new Tile(3,  3, 1, 1, 0, 0, true));
		tiles.put("Leaves",				new Tile(5,  3, 1, 1, 0, 0, true));
		tiles.put("White Wool",			new Tile(0,  4, 1, 1, 0, 0, true));
		tiles.put("Snow Block",			new Tile(2,  4, 1, 1, 0, 0, true));
		tiles.put("Ice Block",			new Tile(3,  4, 1, 1, 0, 0, true));
		tiles.put("Snowy Grass",		new Tile(4,  4, 1, 1, 0, 0, true));
		tiles.put("Cactus",				new Tile(6,  4, 1, 1, 0, 0, true));
		tiles.put("Sugar Cane",			new Tile(9,  4, 1, 1, 0, 0, true));
		tiles.put("Record Player",		new Tile(10, 4, 1, 1, 0, 0, true));
		tiles.put("Torch",				new Tile(0,  5, 1, 1, 1, 14, false));
		tiles.put("Wooden Door",		new Tile(1,  5, 1, 2, 0, 0, true));
		tiles.put("Iron Door",			new Tile(2,  5, 1, 2, 0, 0, true));
		tiles.put("Ladder",				new Tile(3,  5, 1, 1, 0, 0, true));
		tiles.put("Trap Door",			new Tile(4,  5, 1, 1, 0, 0, true));
		tiles.put("Lever",				new Tile(0,  6, 1, 1, 0, 0, true));
	}
	
	/**
	 * Construct a Tile with the specified location and size.
	 * 
	 * @param x The horizontal offset in the SpriteSheet.
	 * @param y The vertical offset in the SpriteSheet.
	 * @param cols The amount of columns the Tile takes up on the
	 * 		SpriteSheet.
	 * @param rows The amount of rows the Tile takes up on the
	 * 		SpriteSheet.
	 * @param transparency The value of transparency from (0 - 1).
	 * @param collidable Whether or not the Tile collides with Actors.
	 */
	public Tile(int x, int y, int cols, int rows, float transparency, float light, boolean collidable)
	{
		this.x            = x;
		this.y            = y;
		this.cols         = cols;
		this.rows         = rows;
		
		this.transparency = transparency;
		this.light        = light;
		
		this.collidable   = collidable;
	}
	
	/**
	 * Get whether or not this Tile emits light.
	 * 
	 * @return Whether or not this Tile emits light.
	 */
	public boolean emitsLight()
	{
		return light > 0;
	}
	
	/**
	 * Get the amount of tiles that the Tile emits light for.
	 * 
	 * @return The amount of tiles that the Tile emits light for.
	 */
	public float getLight()
	{
		return light;
	}
	
	/**
	 * @return Whether or not the Tile collides with Actors.
	 */
	public boolean isCollidable()
	{
		return collidable;
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
	 * @return The value of transparency from (0 - 1).
	 */
	public float getTransparency()
	{
		return transparency;
	}
	
	/**
	 * Get an instance of a Tile with the specified name.
	 * 
	 * @param name The name of the Tile to get.
	 * @return An instance of the Tile with the specified Name.
	 */
	public static Tile getTile(String name)
	{
		return tiles.get(name);
	}
	
	/**
	 * @return The size of a Tile in pixels.
	 */
	public static int getTileSize()
	{
		return tileSize;
	}
	
	/**
	 * @return The SpriteSheet used for rendering the terrain.
	 */
	public static SpriteSheet getTerrainSprites()
	{
		return terrainSprites;
	}
}