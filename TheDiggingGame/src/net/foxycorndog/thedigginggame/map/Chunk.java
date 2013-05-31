package net.foxycorndog.thedigginggame.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Buffer;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.jfoxylib.util.Bounds;
import net.foxycorndog.jfoxylib.util.Intersects;
import net.foxycorndog.jfoxylib.util.Point;
import net.foxycorndog.thedigginggame.TheDiggingGame;
import net.foxycorndog.thedigginggame.actor.Actor;
import net.foxycorndog.thedigginggame.tile.Tile;

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
public class Chunk implements Serializable
{
	private boolean				lightingChanged, tilesChanged;
	
	private int					relativeX, relativeY;
	
	private Buffer				texturesBuffer;
	
	private Bundle				chunkBundle;
	private Bundle				lightingBundle;
	
	private Map					map;
	
	private float				colors[];
	
	private Tile				tiles[];
	
	private ArrayList<Thread>	generateHooks;
	
	private ArrayList<NewTile>	newTiles;
	
	private	ArrayList<Chunk>	calculateLightChunks;
	
	private static Texture		black;
	
	private static Buffer		verticesBuffer;

	public static final int		CHUNK_SIZE			= 32;
	public static final int		LAYER_COUNT			= CHUNK_SIZE * CHUNK_SIZE;
	public static final int		VERTEX_SIZE			= 2;
	public static final int		TILE_COUNT			= LAYER_COUNT * 3;
	public static final int		CHUNK_VERT_COUNT	= TILE_COUNT * 3 * 2 * 3;
	public static final int		BACKGROUND			= 0, MIDDLEGROUND = 1, FOREGROUND = 2;
	
	static
	{
		BufferedImage img = new BufferedImage(Tile.getTileSize(), Tile.getTileSize(), BufferedImage.BITMASK);
		
		Graphics2D g = img.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.dispose();
		
		black = new Texture(img);
		
		verticesBuffer = new Buffer(3 * 2 * 3 * CHUNK_SIZE * CHUNK_SIZE * VERTEX_SIZE);
		
		verticesBuffer.beginEditing();
		{
			int offset = 0;
			
			int tileSize = Tile.getTileSize();
			
			for (int z = -5; z <= 5; z += 5)
			{
				for (int y = 0; y < CHUNK_SIZE; y++)
				{
					for (int x = 0; x < CHUNK_SIZE; x++)
					{
						float verts[] = null;
						
						if (VERTEX_SIZE == 2)
						{
							verts = GL.genRectVerts(x * tileSize, y * tileSize, tileSize, tileSize);
						}
						else if (VERTEX_SIZE == 3)
						{
							verts = GL.genRectVerts(x * tileSize, y * tileSize, z, tileSize, tileSize);
						}
						
						verticesBuffer.setData(offset, verts);
						
						offset += verts.length;
					}
				}
			}
		}
		verticesBuffer.endEditing();
	}
	
	/**
	 * Class that holds the information for a new Tile01 that will be
	 * added to the tiles when the update() method is called.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Feb 22, 2013 at 8:59:20 PM
	 * @since	v0.1
	 * @version Feb 22, 2013 at 11:52:20 PM
	 * @version	v0.1
	 */
	private class NewTile
	{
		private int		x, y;
		private int		layer;
		
		private Tile	tile;
		
		public NewTile(Tile tile, int x, int y, int layer)
		{
			this.tile  = tile;
			
			this.x     = x;
			this.y     = y;
			
			this.layer = layer;
		}
	}
	
	/**
	 * Class that holds the information for a new Tile01 that will be
	 * added to the tiles when the update() method is called.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Feb 22, 2013 at 7:18:20 PM
	 * @since	v0.1
	 * @version Feb 22, 2013 at 7:18:20 PM
	 * @version	v0.1
	 */
	private class LightSource
	{
		private int x, y;
		private int strength;
		
		public LightSource()
		{
			
		}
	}
	
	/**
	 * Class that holds the information for an intersection with an
	 * Actor and a Chunk.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Mar 4, 2013 at 4:15:18 PM
	 * @since	v0.1
	 * @version Mar 4, 2013 at 4:15:18 PM
	 * @version	v0.1
	 */
	class Intersections
	{
		private int		x, y;
		private int 	width, height;
		
		private Point	points[];
		
		public Intersections(int x, int y, int width, int height, Point points[])
		{
			this.x      = x;
			this.y      = y;
			this.width  = width;
			this.height = height;
			
			this.points = points;
		}
		
		/**
		 * @return The horizontal location of the Intersection.
		 */
		public int getX()
		{
			return x;
		}
		
		/**
		 * @return The vertical location of the Intersection.
		 */
		public int getY()
		{
			return y;
		}
		
		/**
		 * @return The horizontal size of the Intersection.
		 */
		public int getWidth()
		{
			return width;
		}
		
		/**
		 * @return The vertical size of the Intersection.
		 */
		public int getHeight()
		{
			return height;
		}
		
		/**
		 * @return The Points of each intersection.
		 */
		public Point[] getPoints()
		{
			return points;
		}
	}
	
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
		this.map       = map;
		
		this.relativeX = rx;
		this.relativeY = ry;
		
		this.colors    = new float[3 * 2 * 4 * LAYER_COUNT];
		
		texturesBuffer = new Buffer(2 * CHUNK_VERT_COUNT);
		
//		float data[] = new float[colorsBuffer.getSize()];
		
		for (int i = 0; i < colors.length; i += 4)
		{
			colors[i + 0] = 1;
			colors[i + 1] = 1;
			colors[i + 2] = 1;
			colors[i + 3] = 0;
		}
		
//		colorsBuffer.beginEditing();
//		{
//			colorsBuffer.setData(0, data);
//		}
//		colorsBuffer.endEditing();
		
		chunkBundle = new Bundle(verticesBuffer, texturesBuffer, null, VERTEX_SIZE);
		
		lightingBundle = new Bundle(CHUNK_SIZE * CHUNK_SIZE * 3 * 2, 2, true, true);
		
		lightingBundle.beginEditingTextures();
		{
			for (int i = 0; i < CHUNK_SIZE * CHUNK_SIZE; i++)
			{
				lightingBundle.addTextures(GL.genRectTextures(black.getImageOffsets()));
			}
		}
		lightingBundle.endEditingTextures();
		
		lightingBundle.beginEditingVertices();
		{
			int ts = Tile.getTileSize();
			
			for (int i = 0; i < CHUNK_SIZE * CHUNK_SIZE; i++)
			{
				lightingBundle.addVertices(GL.genRectVerts(i % CHUNK_SIZE * ts, i / CHUNK_SIZE * ts, ts, ts));
			}
		}
		lightingBundle.endEditingVertices();
		
		tiles    = new Tile[CHUNK_SIZE * CHUNK_SIZE * 3];
		newTiles = new ArrayList<NewTile>();
		
		generateHooks = new ArrayList<Thread>();
		
		updateLighting(true);
	}
	
	/**
	 * Method that generates the terrain of the chunk.
	 */
	public void generate()
	{
		if (relativeY == 0 || relativeY < 0)
		{
			for (int y = 0; y < CHUNK_SIZE - 21; y++)
			{
				for (int x = 0; x < CHUNK_SIZE; x++)
				{
					addTile(Tile.getTile("Dirt"), x, y, MIDDLEGROUND, true);
					addTile(Tile.getTile("Dirt"), x, y, BACKGROUND, true);
				}
			}
			
			if (relativeY < 0)
			{
				for (int y = CHUNK_SIZE - 21; y < CHUNK_SIZE; y++)
				{
					for (int x = 0; x < CHUNK_SIZE; x++)
					{
						addTile(Tile.getTile("Dirt"), x, y, MIDDLEGROUND, true);
						addTile(Tile.getTile("Dirt"), x, y, BACKGROUND, true);
					}
				}
			}
			else
			{
				for (int i = 0; i < CHUNK_SIZE; i++)
				{
					addTile(Tile.getTile("Grass"), i, CHUNK_SIZE - 21, MIDDLEGROUND, true);
					addTile(Tile.getTile("Grass"), i, CHUNK_SIZE - 21, BACKGROUND, true);
				}
			}
		}
		
		calculateTiles();
		
		while (generateHooks.size() > 0)
		{
			Thread hook = generateHooks.remove(0);
			
			hook.start();
		}
	}
	
	/**
	 * Add a Thread hook that will be called after the generation of
	 * this Chunk has finished.
	 * 
	 * @param hook The Thread hook instance to add.
	 */
	public void addGenerateHook(Thread hook)
	{
		generateHooks.add(hook);
	}
	
	/**
	 * Adds a Tile01 to the newTile01 queue. The update() method has to be
	 * called after this call to update the Buffers.
	 * 
	 * @param tile The tile to add to the queue.
	 * @param x The horizontal location of the new Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @param y The vertical location of the new Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @return Whether a Tile01 was successfully added or not.
	 */
	public boolean addTile(Tile tile, int x, int y, int layer, boolean replace)
	{
		if (!replace && tiles[layer * LAYER_COUNT + (x + y * CHUNK_SIZE)] != null)
		{
			return false;
		}
		
		boolean added = newTiles.add(new NewTile(tile, x, y, layer));
		
		lightingChanged = true;
		tilesChanged = true;
		
		return added;
	}

	/**
	 * Removes a Tile01 from the Chunk at the specified location. Still
	 * needs an update() call afterwards.
	 * 
	 * @param x The horizontal location of the Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @param y The vertical location of the Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @return Whether a Tile01 was successfully removed or not.
	 */
	public boolean removeTile(int x, int y, int layer)
	{
		if (tiles[layer * LAYER_COUNT + (x + y * CHUNK_SIZE)] == null)
		{
			return false;
		}
		
		boolean removed = newTiles.add(new NewTile(null, x, y, layer));
		
		lightingChanged = true;
		tilesChanged = true;
		
		return removed;
	}
	
	/**
	 * Get the Tile01 at the specified location in the Chunk.
	 * 
	 * @param x The horizontal offset of the Tile.
	 * @param y The vertical offset of the Tile.
	 * @param layer The layer to get the Tile01 from.
	 * @return The Tile01 at the location.
	 */
	public Tile getTile(int x, int y, int layer)
	{
		return tiles[layer * LAYER_COUNT + x + y * CHUNK_SIZE];
	}
	
//	/**
//	 * Calculates all of the lighting in the Chunk.
//	 */
//	public void calculateLighting(boolean force)
//	{
//		if (!force && !lightingChanged)
//		{
//			return;
//		}
//		
////		colors   = new float[4 * 4 * LAYER_COUNT];
////		bgColors = new float[4 * 4 * LAYER_COUNT];
//		
//		for (int i = 0; i < LAYER_COUNT; i++)
//		{
//			int x = i % CHUNK_SIZE;
//			int y = i / CHUNK_SIZE;
//			
//			Tile bgTile = tiles[i];
//			Tile mgTile = tiles[LAYER_COUNT + i];
//			Tile fgTile = tiles[LAYER_COUNT * 2 + i];
//			
//			float   lightness = 1;
//			
//			int offset = i * 4 * 4;
//			
//			int index = y + 1;
//			
//			Tile above = null;
//			
//			boolean chunkAvailable = map.isChunkAt(this, x, index);
//			
//			while (chunkAvailable && lightness > 0)
//			{
//				above = map.getTile(this, x, index, MIDDLEGROUND);
//				
//				if (above != null)
//				{
//					lightness -= 0.18f * (1 - above.getTransparency());
//				}
//				
//				chunkAvailable = map.isChunkAt(this, x, ++index);
//			}
//			
//			setRGBA(1, 1, 1, 1 - lightness, offset);
//		}
//		
//		calculateLightChunks = new ArrayList<Chunk>();
//		
//		for (int i = 0; i < LAYER_COUNT; i++)
//		{
//			int   x      = i % CHUNK_SIZE;
//			int   y      = i / CHUNK_SIZE;
//			
//			Tile  bgTile = tiles[i];
//			Tile  mgTile = tiles[LAYER_COUNT + i];
//			Tile  fgTile = tiles[LAYER_COUNT * 2 + i];
//			
//			float light  = 0;
//			
//			if (bgTile != null) light = bgTile.getLight() > light ? bgTile.getLight() : light;
//			if (mgTile != null) light = mgTile.getLight() > light ? mgTile.getLight() : light;
//			if (fgTile != null) light = fgTile.getLight() > light ? fgTile.getLight() : light;
//			
//			if (light > 0)
//			{
//				int ceilLight = (int)Math.ceil(light) + 1;
//				
//				int x2        = 0;
//				int y2        = 0;
//				
//				for (int y3 = 0; y3 < ceilLight; y3++)
//				{
//					for (int x3 = 0; x3 < ceilLight; x3++)
//					{
//						x2          = (int)(x + x3 - (light / 2));
//						y2          = (int)(y + y3 - (light / 2));
//						
//						double dist = distance(x, y, x2, y2);
//						
//						float  dif  = -(float)(((light/2) - dist) / (light / 2));
//						
//						dif = dif > 0 ? 0 : dif;
//						
//						addRGBA(0, 0, 0, dif, x2, y2);
//					}
//				}
//			}
//		}
//		
//		while (calculateLightChunks.size() > 0)
//		{
//			Chunk chunk = calculateLightChunks.remove(0);
////			chunk.lightingChanged = true;
//		}
//		
//		lightingChanged = true;
//	}

	/**
	 * Updates all of the lighting in the Chunk and puts it into action.
	 * 
	 * @param force Whether or not to force the action of updating the
	 * 		lighting buffers.
	 */
	public void updateLighting(boolean force)
	{
		if (!force && !lightingChanged)
		{
			return;
		}
//		System.out.println(relativeX + ", " + relativeY);
		
		lightingBundle.beginEditingColors();
		{
			lightingBundle.setColors(0, colors);
		}
		lightingBundle.endEditingColors();
		
		lightingChanged = false;
		
//		colors   = null;
//		bgColors = null;
	}
	
//	/**
//	 * Set the RGBA values for a float array.
//	 * 
//	 * @param colors The float array to set the values on.
//	 * @param r The red component.
//	 * @param g The green component.
//	 * @param b The blue component.
//	 * @param a The alpha component.
//	 * @param offset The offset in the array to set the values at.
//	 */
//	private void setRGBA(float r, float g, float b, float a, int offset)
//	{
//		for (int j = 0; j < 4 * 4; j += 4)
//		{
//			colors[offset + j + 0] = r;
//			colors[offset + j + 1] = g;
//			colors[offset + j + 2] = b;
//			colors[offset + j + 3] = a;
//		}
//	}
	
	/**
	 * Add the RGBA values to the old RGBA values of the float array.
	 * 
	 * @param colors The float array to add the values on.
	 * @param r The red component.
	 * @param g The green component.
	 * @param b The blue component.
	 * @param a The alpha component.
	 * @param x The horizontal location of the Tile to add the
	 * 		values at.
	 * @param y The vertical location of the Tile to add the
	 * 		values at.
	 */
	private void addRGBA(float r, float g, float b, float a, int x, int y)
	{
		if (x < 0 || y < 0 || x >= CHUNK_SIZE || y >= CHUNK_SIZE)
		{
			Chunk chunk = map.getChunkAt(this, x, y);
			
			Bounds bounds = map.checkNegativeLocation(x, y, getRelativeX(), getRelativeY());
			
			x  = bounds.getX();
			y  = bounds.getY();

			x %= Chunk.CHUNK_SIZE;
			y %= Chunk.CHUNK_SIZE;
			
			System.out.println(x + ", " + y);
			
			chunk.addRGBA(r, g, b, a, x, y);
			
			if (!calculateLightChunks.contains(chunk))
			{
				calculateLightChunks.add(chunk);
			}
			
			return;
		}
		
		addRGBA(r, g, b, a, (x + y * CHUNK_SIZE) * 4 * 4);
	}
	
	/**
	 * Add the RGBA values to the old RGBA values of the float array.
	 * 
	 * @param colors The float array to add the values on.
	 * @param r The red component.
	 * @param g The green component.
	 * @param b The blue component.
	 * @param a The alpha component.
	 * @param offset The offset in the array to add the values at.
	 */
	private void addRGBA(float r, float g, float b, float a, int offset)
	{
		if (offset < 0 || offset + 4 * 4 >= colors.length)
		{
			return;
		}
		
		for (int j = 0; j < 4 * 4; j += 4)
		{
			colors[offset + j + 0] += r;
			colors[offset + j + 1] += g;
			colors[offset + j + 2] += b;
			colors[offset + j + 3] += a;
			
			float oldR = colors[offset + j + 0];
			float oldG = colors[offset + j + 1];
			float oldB = colors[offset + j + 2];
			float oldA = colors[offset + j + 3];
			
//			colors[offset + j + 0] = oldR > 1 ? 1 : oldR;
//			colors[offset + j + 1] = oldG > 1 ? 1 : oldG;
//			colors[offset + j + 2] = oldB > 1 ? 1 : oldB;
//			colors[offset + j + 3] = oldA > 1 ? 1 : oldA;
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
	
	/**
	 * Whether the specified Actor is located in the Chunk.
	 * 
	 * @param actor The Actor to check.
	 * @return Whether the Actor is in the Chunk.
	 */
	public boolean inChunk(Actor actor)
	{
		return Intersects.rectangles(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight(), getX(), getY(), getWidth(), getHeight());
	}
	
//	/**
//	 * Get the level of lightness ranging from 0.0-1.0 at the specified
//	 * location. 
//	 * 
//	 * @param x The horizontal location of the Tile.
//	 * @param y The vertical location of the Tile.
//	 * @return The float value of the lightness of the Tile01 at the
//	 * 		specified location.
//	 */
//	public float getLightness(int x, int y)
//	{
//		float lightness = 1;
//		
////		colors[4 * 4 * (x2 + y2 * Chunk.CHUNK_SIZE)];
//		lightness = colors[4 * 4 * (x + y * Chunk.CHUNK_SIZE)];
//		
//		return lightness;
//	}
	
	/**
	 * Calculate all of the new tiles and add then to the Tiles array.
	 */
	public void calculateTiles()
	{
		if (!tilesChanged)
		{
			return;
		}
		
		int size = newTiles.size();
		
		for (int i = size - 1; i >= 0; i--)
		{
			NewTile newTile = newTiles.get(i);
			
//			System.out.println(newTile);
			int x      = newTile.x;
			int y      = newTile.y;
			
			int offset = 3 * 2 * LAYER_COUNT * newTile.layer;
			
			Tile tile  = newTile.tile;
			
			tiles[LAYER_COUNT * newTile.layer + (x + y * CHUNK_SIZE)] = tile;
		}
		
//		if (size > 0)
//		{
//			tilesChanged = false;
//		}
	}
	
	/**
	 * Method that updates the textures of the new Tiles.
	 */
	public void updateTiles()
	{
		if (!tilesChanged)
		{
			return;
		}
		
		chunkBundle.beginEditingTextures();
		{
			while (newTiles.size() > 0)
			{
				NewTile newTile = newTiles.remove(0);
				
				int x      = newTile.x;
				int y      = newTile.y;
				
				int offset = 3 * 2 * LAYER_COUNT * newTile.layer * 2;
				
				Tile tile  = newTile.tile;
				
				float textures[] = null;
				
				if (tile == null)
				{
					textures = new float[3 * 2 * 2];
				}
				else
				{
					textures = GL.genRectTextures(Tile.getTerrainSprites().getImageOffsets(tile.getX(), tile.getY(), tile.getCols(), tile.getRows()));
				}
				
				chunkBundle.setTextures(offset + (x + y * CHUNK_SIZE) * 3 * 2 * 2, textures);
			}
		}
		chunkBundle.endEditingTextures();
		
		tilesChanged = false;
	}
	
	/**
	 * Get the Tile01 array that holds the information of the Tiles in the
	 * Chunk.
	 * 
	 * @return The Tile01 array.
	 */
	public Tile[] getTiles()
	{
		return tiles;
	}
	
	/**
	 * Set the tiles in the Chunk to the specified array.
	 * 
	 * @param tiles The tiles to replace the old ones.
	 */
	public void setTiles(Tile tiles[])
	{
		for (int layer = 0; layer < 3; layer++)
		{
			int offset = layer * LAYER_COUNT;
			
			for (int index = 0; index < LAYER_COUNT; index++)
			{
				addTile(tiles[index + offset], index % CHUNK_SIZE, index / CHUNK_SIZE, layer, true);
			}
		}
	}
	
	/**
	 * Update everything that needs updating in the Chunk.
	 */
	public void update()
	{
		calculateTiles();
		updateTiles();
//		calculateLighting(false);
		updateLighting(false);
	}
	
	/**
	 * Method that renders all of the Tiles to the screen.
	 */
	public void render()
	{
		GL.pushMatrix();
		{
			GL.translate(relativeX * CHUNK_SIZE * Tile.getTileSize(), relativeY * CHUNK_SIZE * Tile.getTileSize(), 0);
			
			renderBackground();
			renderMiddleground();
			renderForeground();
		}
		GL.popMatrix();
	}
	
	/**
	 * Method that renders all of the lighting over the Tiles to the screen.
	 */
	public void renderLighting()
	{
		GL.pushMatrix();
		{
			GL.translate(relativeX * CHUNK_SIZE * Tile.getTileSize(), relativeY * CHUNK_SIZE * Tile.getTileSize(), 0);

			GL.translate(0, 0, 6);
			
			lightingBundle.render(GL.TRIANGLES, black);
		}
		GL.popMatrix();
	}
	
	/**
	 * Method that renders the background Tiles.
	 */
	private void renderBackground()
	{
		GL.pushMatrix();
		{
			GL.translate(0, 0, -5);
			
			GL.setColor(0.4f, 0.4f, 0.4f, 1);
			
			chunkBundle.render(GL.TRIANGLES, 0, 3 * 2 * CHUNK_SIZE * CHUNK_SIZE, Tile.getTerrainSprites());
			
			GL.setColor(1, 1, 1, 1);
		}
		GL.popMatrix();
	}

	/**
	 * Method that renders the middleground Tiles.
	 */
	private void renderMiddleground()
	{
		GL.pushMatrix();
		{
			chunkBundle.render(GL.TRIANGLES, 3 * 2 * CHUNK_SIZE * CHUNK_SIZE, 3 * 2 * CHUNK_SIZE * CHUNK_SIZE, Tile.getTerrainSprites());
		}
		GL.popMatrix();
	}

	/**
	 * Method that renders the foreground Tiles.
	 */
	private void renderForeground()
	{
		GL.pushMatrix();
		{
			GL.translate(0, 0, 5);
			
			chunkBundle.render(GL.TRIANGLES, 3 * 2 * CHUNK_SIZE * CHUNK_SIZE * 2, 3 * 2 * CHUNK_SIZE * CHUNK_SIZE, Tile.getTerrainSprites());
		}
		GL.popMatrix();
	}
	
	/**
	 * @return The horizontal location relative to the origin.
	 */
	public int getRelativeX()
	{
		return relativeX;
	}

	/**
	 * @return The vertical location relative to the origin.
	 */
	public int getRelativeY()
	{
		return relativeY;
	}

	/**
	 * @return The horizontal location relative to the origin in pixels.
	 */
	public int getX()
	{
		return relativeX * CHUNK_SIZE * Tile.getTileSize();
	}

	/**
	 * @return The vertical location relative to the origin in pixels.
	 */
	public int getY()
	{
		return relativeY * CHUNK_SIZE * Tile.getTileSize();
	}

	/**
	 * @return The width of the Chunk in pixels.
	 */
	public static int getWidth()
	{
		return CHUNK_SIZE * Tile.getTileSize();
	}

	/**
	 * @return The height of the Chunk in pixels.
	 */
	public static int getHeight()
	{
		return CHUNK_SIZE * Tile.getTileSize();
	}
	
	public Intersections getIntersections(Actor actor)
	{
		float actorX    = actor.getX() + 1;
		float actorY    = actor.getY();
		int actorWidth  = actor.getWidth() - 2;
		int actorHeight = actor.getHeight() - 1;
		
		float chunkX    = getX();
		float chunkY    = getY();
		
		int startX = 0;
		int startY = 0;
		int width  = 0;
		int height = 0;
		
		int tileSize = Tile.getTileSize();
		
		startX = (int)((actorX - chunkX)  / tileSize) - 1;
		startY = (int)((actorY - chunkY)  / tileSize) - 1;
		width  = (int)(Math.ceil(((float)actorWidth  / tileSize)) + 2);
		height = (int)(Math.ceil(((float)actorHeight / tileSize)) + 2);
		
		startX = startX < 0 ? 0 : startX;
		startY = startY < 0 ? 0 : startY;
		width  = width  + startX >= CHUNK_SIZE ? CHUNK_SIZE - 1 : width;
		height = height + startY >= CHUNK_SIZE ? CHUNK_SIZE - 1 : height;
		
		width  = startX + width  >= CHUNK_SIZE ? CHUNK_SIZE - startX : width;
		height = startY + height >= CHUNK_SIZE ? CHUNK_SIZE - startY : height;
		
		Point points[] = new Point[width * height];
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				points[x + y * width] = new Point(x + startX, y + startY);
			}
		}
		
		Intersections intersections = new Intersections(startX, startY, width, height, points);
		
		return intersections;
	}
	
	/**
	 * Checks whether there is a collision with the Actor and any of the
	 * Tiles.
	 * 
	 * @param actor The Actor to check collisions on.
	 * @return Whether there is a collision.
	 */
	public boolean isCollision(Actor actor)
	{
		float actorX    = actor.getX() + 1;
		float actorY    = actor.getY();
		int actorWidth  = actor.getWidth() - 2;
		int actorHeight = actor.getHeight() - 1;
		
		float chunkX    = getX();
		float chunkY    = getY();
		
		int tileSize = Tile.getTileSize();
		
		if (inChunk(actor))
		{
			Intersections intersections = getIntersections(actor);
			
			Point points[] = intersections.points;
			
			int offset = LAYER_COUNT * MIDDLEGROUND;
			
			for (int i = points.length - 1; i >= 0; i--)
			{
				int x = points[i].getX();
				int y = points[i].getY();
				
				Tile tile = tiles[offset + x + y * CHUNK_SIZE];
				
				if (tile != null && tile.isCollidable())
				{
					if (Intersects.rectangles(x * tileSize + chunkX, y * tileSize + chunkY, tileSize, tileSize, actorX, actorY, actorWidth, actorHeight))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
}