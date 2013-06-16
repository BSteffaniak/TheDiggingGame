package net.foxycorndog.thedigginggame.map.terrain;

import java.util.Random;

import net.foxycorndog.thedigginggame.item.tile.Tile;
import net.foxycorndog.thedigginggame.map.Chunk;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Jun 14, 2013 at 6:58:41 PM
 * @since	v0.4
 * @version	Jun 14, 2013 at 6:58:41 PM
 * @version	v0.4
 */
public class TerrainMap
{
	private					int 	biome;
//	private					Tile	tiles[];
	
	private			final	int		WIDTH, HEIGHT;
	
	private	static	final	float	PI = 3.14159f;
	
	private	static	final	Random	random	= NoiseMap.random;
	
	public	static	final	int		PLAINS	= 1, DESERT = 2;
	
	public TerrainMap(int width, int height, int biome)
	{
		this.WIDTH  = width;
		this.HEIGHT = height;
		this.biome  = biome;
		
//		tiles       = new Tile[width * height * 3];
	}
	
//	/**
//	 * Get the array of Tiles that describes the generated TerrainMap.
//	 * 
//	 * @return The array of Tiles that describes the generated TerrainMap.
//	 */
//	public Tile[] getTiles()
//	{
//		return tiles;
//	}
	
	public void generate(Chunk chunk)
	{
		Tile commonTile     = getCommonTile();
		Tile surfaceTile    = getSurfaceTile();
		Tile midSurfaceTile = getMidSurfaceTile();
		
		int values[] = null;
		
		if (chunk.isSurface())
		{
			values = chunk.getNoiseMap().getValues();
		}
		else if (chunk.isGround())
		{
			values = new int[WIDTH];
			
			for (int i = 0; i < values.length; i++)
			{
				values[i] = WIDTH - 1;
			}
		}
		else if (chunk.isAir())
		{
			values = new int[WIDTH];
			
			for (int i = 0; i < values.length; i++)
			{
				values[i] = -1;
			}
		}
		
		for (int x = 0; x < WIDTH; x++)
		{
			for (int y = 0; y <= values[x]; y++)
			{
				chunk.addTile(commonTile, x, y, Chunk.BACKGROUND, false);
				chunk.addTile(commonTile, x, y, Chunk.MIDDLEGROUND, false);
//				chunk.addTile(commonTile, x, y, Chunk.FOREGROUND, true);
			}
		}
		
		chunk.calculateTiles();
		
		if (chunk.isSurface())
		{
			for (int x = 0; x < WIDTH; x++)
			{
				int  hei     = values[x] - getSurfaceHeight() - getMidSurfaceHeight();
				
				int  counter = 0;
				
				Tile tile    = getSurfaceTile();
				
				for (int y = HEIGHT; y >= hei; y--)
				{
					if (y <= values[x])
					{
						if (counter >= getSurfaceHeight())
						{
							tile = getMidSurfaceTile();
						}
						
						counter++;
	
						chunk.addTile(tile, x, y, Chunk.BACKGROUND, true);
						chunk.addTile(tile, x, y, Chunk.MIDDLEGROUND, true);
//						chunk.addTile(tile, x, y, Chunk.FOREGROUND, true);
					}
//					else
//					{
//						chunk.removeTile(x, y, Chunk.BACKGROUND);
//						chunk.removeTile(x, y, Chunk.MIDDLEGROUND);
//						chunk.removeTile(x, y, Chunk.FOREGROUND);
//					}
				}
			}
		}
		
		chunk.calculateTiles();
		
		addVeins(chunk);
		
		chunk.calculateTiles();
	}
	
	private void addVeins(Chunk chunk)
	{
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				if (random.nextInt(30) == 0)
				{
					Tile tile = Tile.getRandomTile();
					
					while (!tile.isVein())
					{
						tile = Tile.getRandomTile();
					}
					
					addVein(x, y, tile, chunk);
				}
			}
		}
	}
	
	private void addVein(int x, int y, Tile tile, Chunk chunk)
	{
		int min = (int)Math.round(Math.sqrt(tile.getMinVein() / PI) * 2 / 2);
		int max = (int)Math.round(Math.sqrt(tile.getMaxVein() / PI) * 2 / 2);
		
		int xOff = random.nextInt(max);
		int yOff = random.nextInt(max);
		int size = random.nextInt(max - min) + min;
		
		fillCircle(x + xOff, y + yOff, size, tile, chunk);
		
		xOff = random.nextInt(max);
		yOff = random.nextInt(max);
		size = random.nextInt(max - min) + min;
		
		fillCircle(x + xOff, y + yOff, size, tile, chunk);
	}
	
	private void fillCircle(int x, int y, int size, Tile tile, Chunk chunk)
	{
		float halfSize = size / 2f;
		
		for (int y2 = 0; y2 < size; y2++)
		{
			for (int x2 = 0; x2 < size; x2++)
			{
				int x3 = Math.round(x + x2 - halfSize);
				int y3 = Math.round(y + y2 - halfSize);
				
				if (distance(x, y, x3, y3) <= halfSize)
				{
					if (chunk.getTile(x3, y3, Chunk.MIDDLEGROUND) != null)
					{
						chunk.addTile(tile, x3, y3, Chunk.MIDDLEGROUND, true);
						chunk.addTile(tile, x3, y3, Chunk.BACKGROUND, true);
					}
				}
			}
		}
	}
	
	/**
	 * Get the distance from two points.
	 * 
	 * @param x1 The horizontal position of the first point.
	 * @param y1 The vertical position of the first point.
	 * @param x2 The horizontal position of the second point.
	 * @param y2 The vertical position of the second point.
	 * @return The distance between the two points.
	 */
	private double distance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	private	int getMidSurfaceHeight()
	{
		if (biome == PLAINS)
		{
			return 5;
		}
		else if (biome == DESERT)
		{
			return 3;
		}
		
		return 0;
	}
	
	private	int getSurfaceHeight()
	{
		if (biome == PLAINS)
		{
			return 1;
		}
		else if (biome == DESERT)
		{
			return 3;
		}
		
		return 0;
	}
	
	private Tile getMidSurfaceTile()
	{
		if (biome == PLAINS)
		{
			return Tile.getTile("Dirt");
		}
		else if (biome == DESERT)
		{
			return Tile.getTile("Sandstone");
		}
		
		return null;
	}
	
	private Tile getSurfaceTile()
	{
		if (biome == PLAINS)
		{
			return Tile.getTile("Grass");
		}
		else if (biome == DESERT)
		{
			return Tile.getTile("Sand");
		}
		
		return null;
	}
	
	private Tile getCommonTile()
	{
		if (biome == PLAINS)
		{
			return Tile.getTile("Stone");
		}
		else if (biome == DESERT)
		{
			return Tile.getTile("Stone");
		}
		
		return null;
	}
}
