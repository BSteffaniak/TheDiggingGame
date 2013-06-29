package net.foxycorndog.thedigginggame.map.terrain;

import java.util.Random;

import net.foxycorndog.jfoxylib.util.Bounds;
import net.foxycorndog.thedigginggame.item.tile.Tile;
import net.foxycorndog.thedigginggame.map.Chunk;
import net.foxycorndog.thedigginggame.map.Map;

/**
 * Class used to generate the Tiles that are used to fill
 * the Chunk after its generation with the NoiseMap. After this phase
 * is finished, it then creates caves, foliage, and water pools.
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
	
	private			final	int		width, height;
	
	private	static	final	float	PI = 3.14159f;
	
	private	static	final	Random	random	= NoiseMap.random;
	
	public	static	final	int		PLAINS	= 1, DESERT = 2;
	
	/**
	 * Create a TerrainMap that can be used to generate 
	 * 
	 * @param width
	 * @param height
	 * @param biome
	 */
	public TerrainMap(int width, int height, int biome)
	{
		this.width  = width;
		this.height = height;
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
		generateBackground(chunk);
		
		chunk.calculateTiles();
		
		generateVeins(chunk);
		
		chunk.calculateTiles();
		
		generateCaves(chunk);
		
		chunk.calculateTiles();
		
		generateFoliage(chunk);

		chunk.calculateTiles();
		
		generateWaterPools(chunk);
	}
	
	private void generateBackground(Chunk chunk)
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
			values = new int[width];
			
			for (int i = 0; i < values.length; i++)
			{
				values[i] = width - 1;
			}
		}
		else if (chunk.isAir())
		{
			values = new int[width];
			
			for (int i = 0; i < values.length; i++)
			{
				values[i] = -1;
			}
		}
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y <= values[x]; y++)
			{
				chunk.addTile(commonTile, x, y, Chunk.BACKGROUND, false);
				chunk.addTile(commonTile, x, y, Chunk.MIDDLEGROUND, false);
//				chunk.addTile(commonTile, x, y, Chunk.FOREGROUND, true);
			}
		}
		
		chunk.calculateTiles();
		
		generateSurface(chunk, values);
	}
	
	private void generateSurface(Chunk chunk, int values[])
	{
		if (chunk.isSurface())
		{
			for (int x = 0; x < width; x++)
			{
				int  hei     = getSurfaceHeight() + getMidSurfaceHeight();
				
				int  counter = 0;
				
				Tile tile    = getSurfaceTile();
				
				for (int y = values[x]; counter < hei; y--)
				{
					if (counter >= getSurfaceHeight())
					{
						tile = getMidSurfaceTile();
					}

					chunk.addTile(tile, x, y, Chunk.BACKGROUND, true);
					chunk.addTile(tile, x, y, Chunk.MIDDLEGROUND, true);
//					chunk.addTile(tile, x, y, Chunk.FOREGROUND, true);
					
					counter++;
				}
			}
		}
	}
	
	private void generateVeins(Chunk chunk)
	{
		//TODO: fix this for the individual locations.
		Bounds bounds = Map.trimLocation(0, 0, chunk.getRelativeX(), chunk.getRelativeY());
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (random.nextInt(70) == 0)
				{
					Tile tile = null;
					
					do
					{
						tile = Tile.getRandomTile(bounds.getY());
					}
					while (!tile.isVein());
					
					generateVein(x, y, tile, chunk);
				}
			}
		}
	}
	
	private void generateVein(int x, int y, Tile tile, Chunk chunk)
	{
		float min  = (float)(Math.sqrt(tile.getMinVein() / PI) * 2 / 2);
		float max  = (float)(Math.sqrt(tile.getMaxVein() / PI) * 2 / 2);
		
		int   xOff = random.nextInt(Math.round(max));
		int   yOff = random.nextInt(Math.round(max));
		int   size = Math.round(random.nextInt(Math.round(max - min)) + min);
		
		fillCircle(x + xOff, y + yOff, size, tile, chunk);
		
		xOff = random.nextInt(Math.round(max));
		yOff = random.nextInt(Math.round(max));
		size = Math.round(random.nextInt(Math.round(max - min)) + min);
		
		fillCircle(x + xOff, y + yOff, size, tile, chunk);
	}
	
	private void fillCircle(int x, int y, int size, Tile tile, Chunk chunk)
	{
		float halfSize = size / 2f;
		
		float count = 0;
		
		for (int y2 = -1; y2 < size + 2; y2++)
		{
			for (int x2 = -1; x2 < size + 2; x2++)
			{
				float x3 = x + x2 - halfSize;
				float y3 = y + y2 - halfSize;
				
				if (distance(x, y, x3, y3) <= halfSize * 2f)
				{
					int x4 = Math.round(x3);
					int y4 = Math.round(y3);
					
					if (chunk.getTile(x4, y4, Chunk.MIDDLEGROUND) != null)
					{
						chunk.addTile(tile, x4, y4, Chunk.MIDDLEGROUND, true);
						
						count++;
						
						if (count >= tile.getMaxVein() / 2f)
						{
							return;
						}
						
						chunk.addTile(tile, x4, y4, Chunk.BACKGROUND, true);
						
						count++;
						
						if (count >= tile.getMaxVein() / 2f)
						{
							return;
						}
					}
				}
			}
		}
	}
	
	private void generateCaves(Chunk chunk)
	{
		
	}
	
	private void generateFoliage(Chunk chunk)
	{
		
	}
	
	private void generateWaterPools(Chunk chunk)
	{
		
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
	private double distance(float x1, float y1, float x2, float y2)
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
