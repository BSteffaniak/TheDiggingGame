package net.foxycorndog.thedigginggame.item.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import net.foxycorndog.jbiscuit.item.JTile;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.thedigginggame.TheDiggingGame;
import net.foxycorndog.thedigginggame.item.Item;
import net.foxycorndog.thedigginggame.map.terrain.NoiseMap;

/**
 * Class that holds information for a Tile that is used in the terrain.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 22, 2013 at 4:23:23 AM
 * @since	v0.2
 * @version Feb 22, 2013 at 4:23:24 AM
 * @version	v0.2
 */
public class Tile extends JTile
{
	private			int		minVein, maxVein;
	private			int		rarity;
	
	private			boolean	collidable;
	
	private			float	transparency;
	private			float	light;
	private			float	climbSpeed;
	
	private	static	int		tileSize;
	private	static	int		tileCount;
	
	private	static	Tile	tiles[];
	
	private	static	final	Random	random = NoiseMap.random;
	
	static
	{
		tileSize = 16;
		
		tiles = new Tile[256];
		
		int index = 0;
		
		tiles[index++] = new Tile(1,  0,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Stone", 64);
		tiles[index++] = new Tile(2,  0,  1, 1, 0, 0,  0, 3, 70,  10,  true,  "Dirt", 64);
		tiles[index++] = new Tile(2,  1,  1, 1, 0, 0,  0, 3, 70,  10,  true,  "Sand", 64);
		tiles[index++] = new Tile(3,  1,  1, 1, 0, 0,  0, 3, 70,  10,  true,  "Gravel", 64);
		tiles[index++] = new Tile(3,  0,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Grass", 64);
		tiles[index++] = new Tile(4,  0,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Wooden Planks", 64);
		tiles[index++] = new Tile(5,  0,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Double Stone Slab", 64);
		tiles[index++] = new Tile(6,  0,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Stone Slab", 64);
		tiles[index++] = new Tile(7,  0,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Bricks", 64);
		tiles[index++] = new Tile(8,  0,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "TNT", 64);
		tiles[index++] = new Tile(0,  1,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Cobblestone", 64);
		tiles[index++] = new Tile(1,  1,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Bedrock", 64);
		tiles[index++] = new Tile(4,  1,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Log", 64);
		tiles[index++] = new Tile(6,  1,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Iron Block", 64);
		tiles[index++] = new Tile(7,  1,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Gold Block", 64);
		tiles[index++] = new Tile(8,  1,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Diamond Block", 64);
		tiles[index++] = new Tile(9,  1,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Emerald Block", 64);
		tiles[index++] = new Tile(0,  2,  1, 1, 0, 0,  0, 1, 8,   70,  true,  "Gold Ore", 64);
		tiles[index++] = new Tile(1,  2,  1, 1, 0, 0,  0, 1, 8,   25,  true,  "Iron Ore", 64);
		tiles[index++] = new Tile(2,  3,  1, 1, 0, 0,  0, 1, 8,   400, true,  "Diamond Ore", 64);
		tiles[index++] = new Tile(3,  3,  1, 1, 0, 0,  0, 1, 8,   60,  true,  "Redstone Ore", 64);
		tiles[index++] = new Tile(2,  2,  1, 1, 0, 0,  0, 1, 64,  45,  true,  "Coal Ore", 64);
		tiles[index++] = new Tile(3,  2,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Bookshelf", 64);
		tiles[index++] = new Tile(4,  2,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Mossy Cobblestone", 64);
		tiles[index++] = new Tile(5,  2,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Obsidian", 64);
		tiles[index++] = new Tile(12, 2,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Furnace", 64);
		tiles[index++] = new Tile(14, 2,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Dispenser", 64);
		tiles[index++] = new Tile(0,  3,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Sponge", 64);
		tiles[index++] = new Tile(1,  3,  1, 1, 1, 0,  0, 0, 0,   0,   true,  "Glass", 64);
		tiles[index++] = new Tile(5,  3,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Leaves", 64);
		tiles[index++] = new Tile(0,  4,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "White Wool", 64);
		tiles[index++] = new Tile(2,  4,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Snow Block", 64);
		tiles[index++] = new Tile(3,  4,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Ice Block", 64);
		tiles[index++] = new Tile(4,  4,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Snowy Grass", 64);
		tiles[index++] = new Tile(6,  4,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Cactus", 64);
		tiles[index++] = new Tile(9,  4,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Sugar Cane", 64);
		tiles[index++] = new Tile(10, 4,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Record Player", 1);
		tiles[index++] = new Tile(0,  5,  1, 1, 1, 14, 0, 0, 0,   0,   false, "Torch", 64);
		tiles[index++] = new Tile(1,  5,  1, 2, 0, 0,  0, 0, 0,   0,   true,  "Wooden Door", 8);
		tiles[index++] = new Tile(2,  5,  1, 2, 0, 0,  0, 0, 0,   0,   true,  "Iron Door", 8);
		tiles[index++] = new Tile(3,  5,  1, 1, 1, 0,  1, 0, 0,   0,   false, "Ladder", 64);
		tiles[index++] = new Tile(4,  5,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Trap Door", 64);
		tiles[index++] = new Tile(0,  6,  1, 1, 0, 0,  0, 0, 0,   0,   true,  "Lever", 64);
		tiles[index++] = new Tile(0,  12, 1, 1, 0, 0,  0, 0, 0,   0,   true,  "Sandstone", 64);
		
		tileCount = index;
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
	 * @param stackSize The amount of specific Item instances can be
	 * 		stacked in one Inventory Slot before filling the Slot to
	 * 		its max.
	 */
	public Tile(int x, int y, int cols, int rows, float transparency, float light, float climbSpeed, int minVein, int maxVein, int rarity, boolean collidable, String name, int stackSize)
	{
		super(name, x, y, cols, rows, collidable, stackSize);
		
		this.transparency = transparency;
		this.light        = light;
		this.climbSpeed   = climbSpeed;
		
		this.minVein      = minVein;
		this.maxVein      = maxVein;
		
		this.rarity       = rarity;
		
		this.collidable   = collidable;
	}
	
	/**
	 * Get whether the specified Tile can generate in veins or not.
	 * 
	 * @return Whether the specified Tile can generate in veins or not.
	 */
	public boolean isVein()
	{
		return maxVein > 0;
	}
	
	/**
	 * Get the min number of the specified Tile that can appear in a
	 * vein.
	 * 
	 * @return The min number of the specified Tile that can appear in a
	 * 		vein.
	 */
	public int getMinVein()
	{
		return minVein;
	}
	
	/**
	 * Get the max number of the specified Tile that can appear in a
	 * vein.
	 * 
	 * @return The max number of the specified Tile that can appear in a
	 * 		vein.
	 */
	public int getMaxVein()
	{
		return maxVein;
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
	 * Get the amount of Tiles that the Tile emits light for.
	 * 
	 * @return The amount of Tiles that the Tile emits light for.
	 */
	public float getLight()
	{
		return light;
	}
	
	/**
	 * Get whether the Tile can be climbed by an Actor.
	 * 
	 * @return Whether the Tile can be climbed by an Actor.
	 */
	public boolean isClimbable()
	{
		return climbSpeed > 0;
	}
	
	/**
	 * Get the speed in which an Actor can climb the specified Tile.
	 * 
	 * @return The speed in which an Actor can climb the specified Tile.
	 */
	public float getClimbSpeed()
	{
		return climbSpeed;
	}
	
	/**
	 * @return Whether or not the Tile collides with Actors.
	 */
	public boolean isCollidable()
	{
		return collidable;
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
		for (int i = 0; i < tiles.length; i++)
		{
			Tile tile = tiles[i];
			
			if (tile == null)
			{
				return null;
			}
			else if (tile.getName().equalsIgnoreCase(name))
			{
				return tile;
			}
		}
		
		return null;
	}
	
	/**
	 * Get a random Tile instance from the Tiles array.
	 * 
	 * @return A random Tile instance from the Tiles array.
	 */
	public static Tile getRandomTile()
	{
		Tile tile = tiles[random.nextInt(tileCount)];
		
		return tile;
	}
	
	/**
	 * Get a random Tile instance from the Tiles array that suits
	 * the depth given.
	 * 
	 * @return A random Tile instance from the Tiles array.
	 */
	public static Tile getRandomTile(int depth)
	{
		Tile tile = null;
		
		do
		{
			tile = tiles[(int)(Math.random() * tileCount)];
		}
		while (tile.rarity > 0 && !((tile.rarity < depth && tile.rarity >= 25 && random.nextInt(Math.round(tile.rarity / 50f)) == 0) || (random.nextInt(tile.rarity + (tile.rarity - depth) * 0) == 0)));
		
		return tile;
	}
	
	/**
	 * @return The size of a Tile in pixels.
	 */
	public static int getTileSize()
	{
		return tileSize;
	}
}