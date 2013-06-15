package net.foxycorndog.thedigginggame.map.terrain;

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
	private					int		depth;
	private					int 	biome;
	private					int		width, height;
	
	private					Tile	tiles[];
	
	public	static	final	int		PLAINS = 1, DESERT = 2;
	
	public TerrainMap(int width, int height, int depth, int biome)
	{
		this.width  = width;
		this.height = height;
		this.depth  = depth;
		this.biome  = biome;
	}
	
	public void generate()
	{
		Tile biomeBg = getBackgroundTile();
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				tiles[x + y * width] = biomeBg;
			}
		}
		
		
	}
	
	private Tile getBackgroundTile()
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
