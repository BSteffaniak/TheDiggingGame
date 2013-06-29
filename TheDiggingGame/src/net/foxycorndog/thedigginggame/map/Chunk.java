package net.foxycorndog.thedigginggame.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import net.foxycorndog.jbiscuit.map.JImageTileChunk;
import net.foxycorndog.jbiscuit.map.JTileChunk;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Buffer;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.jfoxylib.util.Bounds;
import net.foxycorndog.jfoxylib.util.Intersects;
import net.foxycorndog.jfoxylib.util.Point;
import net.foxycorndog.jfoxyutil.Queue;
import net.foxycorndog.thedigginggame.TheDiggingGame;
import net.foxycorndog.thedigginggame.actor.Actor;
import net.foxycorndog.thedigginggame.item.tile.Tile;
import net.foxycorndog.thedigginggame.map.terrain.NoiseMap;
import net.foxycorndog.thedigginggame.map.terrain.TerrainMap;

/**
 * Class that holds the information for each of the chunks.
 * Such information includes the relative location of the chunk,
 * the size of a Chunk, and the tiles in the chunk.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 22, 2013 at 4:23:18 AM
 * @since	v0.1
 * @version Mar 3, 2013 at 4:51:19 PM
 * @version	v0.1
 */
public class Chunk extends JImageTileChunk
{
	private					boolean				generated, generating;
	
	private					int					level;
	
	private					NoiseMap			noiseMap;
	
	private					TerrainMap			terrainMap;

	public	static	final	int					CHUNK_SIZE			= 32;
	public	static	final	int					LAYER_COUNT			= CHUNK_SIZE * CHUNK_SIZE;
	public	static	final	int					VERTEX_SIZE			= 2;
	public	static	final	int					TILE_COUNT			= LAYER_COUNT * 3;
	public	static	final	int					CHUNK_VERT_COUNT	= TILE_COUNT * 3 * 2 * 3;
	public	static	final	int					BACKGROUND			= 0, MIDDLEGROUND = 1, FOREGROUND = 2;
	public	static	final	int					LIGHT				= 0, COLOR = 1, OUTPUT = 2;
	
	/**
	 * Constructs a Chunk in the specified Map at the specified relative
	 * location.
	 * 
	 * @param map The map to add the Chunk to.
	 * @param rx The relative horizontal location.
	 * @param ry The relative vertical location.
	 */
	public Chunk(Map map, int rx, int ry)
	{
		super(map, rx, ry);
		
		Chunk l = map.getChunk(rx - 1, ry);
		Chunk r = map.getChunk(rx + 1, ry);

		noiseMap = new NoiseMap(CHUNK_SIZE, CHUNK_SIZE, l == null ? null : l.noiseMap, r == null ? null : r.noiseMap);
	}
	
	/**
	 * Get the NoiseMap instance that holds the values used to generate
	 * the Terrain levels.
	 * 
	 * @return The NoiseMap instance.
	 */
	public NoiseMap getNoiseMap()
	{
		return noiseMap;
	}
	
	/**
	 * Get whether the Chunk has been and is finished generating or not.
	 * 
	 * @return Whether the Chunk has been and is finished generating or
	 * 		not.
	 */
	public boolean isGenerated()
	{
		return generated;
	}
	
	public void generate(Chunk left, Chunk right)
	{
		generate(left, right, true);
	}
	
	/**
	 * Get whether the specified Chunk is an air Chunk or not.
	 * 
	 * @return Whether the specified Chunk is an air Chunk or not.
	 */
	public boolean isAir()
	{
		return level == 1;
	}
	
	/**
	 * Get whether the specified Chunk is a surface Chunk or not.
	 * 
	 * @return Whether the specified Chunk is a surface Chunk or not.
	 */
	public boolean isSurface()
	{
		return level == 0;
	}
	
	/**
	 * Get whether the specified Chunk is a ground Chunk or not.
	 * 
	 * @return Whether the specified Chunk is a ground Chunk or not.
	 */
	public boolean isGround()
	{
		return level == -1;
	}
	
	/**
	 * Method that generates the terrain of the chunk if it has not
	 * been generated yet.
	 */
	public void generate(Chunk left, Chunk right, boolean regen)
	{
		if (generating)
		{
			return;
		}
		
		generating = true;
		generated  = false;
		
		if (getRelativeY() < 0)
		{
			level = -1;
		}
		else if (getRelativeY() == 0)
		{
			level = 0;
		}
		else if (getRelativeY() > 0)
		{
			level = 1;
		}
		
		if (isSurface())
		{
			NoiseMap l = left  == null ? null : left.noiseMap;
			NoiseMap r = right == null ? null : right.noiseMap;
			
			if (l != null && l.getRight() != null)
			{
				noiseMap = l.getRight();
			}
			else if (r != null && r.getLeft() != null)
			{
				noiseMap = r.getLeft();
			}
			
			if (regen)
			{
				noiseMap.generate();
			}
			
			if (left != null)
			{
				left.noiseMap.setRight(noiseMap);
				
				if (regen && left.generated)
				{
					left.generate((Chunk)left.getLeft(), this, false);
				}
			}
			if (right != null)
			{
				right.noiseMap.setLeft(noiseMap);

				if (regen && right.generated)
				{
					right.generate(this, (Chunk)right.getRight(), false);
				}
			}
		}
		
		terrainMap = new TerrainMap(CHUNK_SIZE, CHUNK_SIZE, TerrainMap.DESERT);
		
		terrainMap.generate(this);
		
		calculateTiles();
		initLighting();
		
		super.generate(left, right, regen);
		
		generated  = true;
		generating = false;
	}
	
	public Map getMap()
	{
		return (Map)super.getMap();
	}
	
	/**
	 * Calculate the intensity of the shadow on the Tile at the specified
	 * location.
	 * 
	 * @param x The horizontal location of the Tile.
	 * @param y The vertical location of the Tile.
	 * @return The intensity of the shadow given on the Tile.
	 */
	public float calculateShadow(int x, int y)
	{
		float shadow      = 0;
		
		int   index       = y + 1;
		
		Tile  above       = null;
		
		boolean available = getMap().isChunkAt(this, x, index);
		
		while (available)
		{
			above = (Tile)getMap().getTile(this, x, index, MIDDLEGROUND);
			
			if (above != null)
			{
				shadow += 0.18f * (1 - above.getTransparency());
			}
			
			available = getMap().isChunkAt(this, x, ++index);
		}
		
		return shadow;
	}
	
	/**
	 * Calculate the intensity of the shadow given by the Tile at the
	 * specified location to the specified depth under it.
	 * 
	 * @param x The horizontal location of the Tile.
	 * @param y The vertical location of the Tile.
	 * @param depth The depth to find the shadow of.
	 * @return The intensity of the shadow given by the Tile at the
	 * 		depth.
	 */
	private float calculateShadow(int x, int y, int depth)
	{
		float shadow = 0;
		
		Tile current = (Tile)getTile(x, y, MIDDLEGROUND);
		
		for (int i = 0; i < depth; i++)
		{
			if (current != null)
			{
				shadow += 0.18f * (1 - current.getTransparency());
			}
			
			current = (Tile)getTile(x, y - i, MIDDLEGROUND);
		}
		
		return shadow;
	}
}