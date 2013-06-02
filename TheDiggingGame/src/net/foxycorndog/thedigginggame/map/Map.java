package net.foxycorndog.thedigginggame.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.shader.Shader;
import net.foxycorndog.jfoxylib.util.Bounds;
import net.foxycorndog.jfoxylib.util.Intersects;
import net.foxycorndog.jfoxylib.util.Point;
import net.foxycorndog.jfoxyutil.Queue;
import net.foxycorndog.thedigginggame.TheDiggingGame;
import net.foxycorndog.thedigginggame.actor.Actor;
import net.foxycorndog.thedigginggame.map.Chunk.Intersections;
import net.foxycorndog.thedigginggame.tile.Tile;

/**
 * Class that holds the information of everything on the map. Such things
 * include: Actors, chunks, and location.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 22, 2013 at 4:23:10 AM
 * @since	v0.1
 * @version Feb 22, 2013 at 4:23:10 AM
 * @version	v0.1
 */
public class Map
{
	private boolean			collision;
	private	boolean			generatingChunks;
	
	private float			x, y;
	
	private TheDiggingGame	game;
	
	private Shader			lighting;
	
	private Thread			lightingThread, generatorThread;
	
	private LightingClass	lightingInstance;
	
	private HashMap<Integer, HashMap<Integer, Chunk>> chunks;
	
	private ArrayList<Actor>	actors;
	
	private	Queue<Chunk>		chunkQueue;
	
	/**
	 * Class that has a method that is to be implemented. Used
	 * While iterating through the Chunks.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Feb 22, 2013 at 4:23:10 AM
	 * @since	v0.1
	 * @version Feb 22, 2013 at 4:23:10 AM
	 * @version	v0.1
	 */
	private abstract class Task
	{
		/**
		 * Method to be implemented.
		 * 
		 * @param chunk The chunk that is currently being iterated.
		 */
		public abstract void run(Chunk chunk);
	}
	
	/**
	 * Class that calculates the lighting in the Chunks.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Mar 16, 2013 at 11:02:55 PM
	 * @since	v0.1
	 * @version Mar 16, 2013 at 11:02:55 PM
	 * @version	v0.1
	 */
	private class LightingClass implements Runnable
	{
		/**
		 * Method called when the Thread is started.
		 */
		public void run()
		{
			iterateChunks(new Task()
			{
				public void run(Chunk chunk)
				{
//					chunk.calculateLighting(false);
					//TODO: I did this too.
				}
			});
		}
	}
	
	/**
	 * Construct a Map.
	 */
	public Map(TheDiggingGame game)
	{
		lighting = new Shader();
		lighting.loadFile(TheDiggingGame.getResourcesLocation() + "res/shaders/", new String[] { "lighting.vs" }, new String[] { "lighting.frag" });
		
		lightingInstance = new LightingClass();
		
		chunks     = new HashMap<Integer, HashMap<Integer, Chunk>>();
		
		actors     = new ArrayList<Actor>();
		
		this.game  = game;
		
		chunkQueue = new Queue<Chunk>();
		
		generatorThread = new Thread()
		{
			public void run()
			{
				while (true)
				{
					generatingChunks = false;
					
					synchronized (this)
					{
						try
						{
							wait();
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					
					generatingChunks = true;
					
					for (int i = chunkQueue.size() - 1; i >= 0; i--)
					{
						chunkQueue.peek(i).generate();
					}
					
					while (!chunkQueue.isEmpty())
					{
						Chunk chunk = chunkQueue.dequeue();
//						chunk.calculateLighting(true);
						//TODO: i did this
					}
					
//					System.exit(0);
				}
			}
		};
		
		generatorThread.start();
		
//		lightingThread = new Thread("Lighting")
//		{
//			public void run()
//			{
//				while (true)
//				{
//					synchronized (this)
//					{
//						try
//						{
//							wait();
//						}
//						catch (InterruptedException e)
//						{
//							e.printStackTrace();
//						}
//					}
//					
//					iterateChunks(new Task()
//					{
//						public void run(Chunk chunk)
//						{
//							chunk.calculateLighting();
//						}
//					});
//				}
//			}
//		};
//		
//		lightingThread.setPriority(Thread.MIN_PRIORITY);
//		lightingThread.start();
	}
	
	/**
	 * @return The game in which the Map is located.
	 */
	public TheDiggingGame getGame()
	{
		return game;
	}
	
	/**
	 * @return An ArrayList of all of the Actors in the Map.
	 */
	public ArrayList<Actor> getActors()
	{
		return actors;
	}
	
	/**
	 * Add an actor to the Map if not already added.
	 * 
	 * @param actor The Actor to add to the map.
	 */
	public void addActor(Actor actor)
	{
		if (!actors.contains(actor))
		{
			actors.add(actor);
		}
	}
	
	/**
	 * Used to set an Actor to the center of the Display.
	 * 
	 * @param actor
	 */
	public void setActorFocused(Actor actor)
	{
		actor.setFocused(true);
		
		for (int i = actors.size() - 1; i >= 0; i--)
		{
			if (actors.get(i) != actor)
			{
				actors.get(i).setFocused(false);
			}
		}
	}
	
	/**
	 * Updates all of the lighting for the Chunks.
	 */
	public void calculateLighting()
	{
//		new Thread()
//		{
//			public void run()
//			{
//				synchronized (lightingThread)
//				{
//					lightingThread.notify();
//				}
//			}
//		}.start();
		
		lightingThread = new Thread(lightingInstance);
		
		lightingThread.setPriority(Thread.MIN_PRIORITY);
		lightingThread.start();
		
//		updateActorLighting();
	}
	
//	public synchronized void updateLighting()
//	{
//		iterateChunks(new Task()
//		{
//			public void run(Chunk chunk)
//			{
//				chunk.updateLighting();
//			}
//		});
//	}
	
	/**
	 * Generate a Chunk at the specified location with the specified
	 * Tiles.
	 * 
	 * @param rx The relative horizontal location to add the Chunk to.
	 * @param ry The relative vertical location to add the Chunk to.
	 * @param tiles The Tiles to set in the Chunk.
	 * @return Whether the Chunk was successfully generated or not.
	 */
	public boolean generateChunk(int rx, int ry, Tile tiles[])
	{
		if (!isChunkAt(rx, ry))
		{
			Chunk chunk = new Chunk(this, rx, ry);
			
			chunk.setTiles(tiles);
			
			chunk.update();
//			chunk.calculateLighting();
//			chunk.updateLighting();
			
			if (!chunks.containsKey(rx))
			{
				chunks.put(rx, new HashMap<Integer, Chunk>());
			}
			
			chunks.get(rx).put(ry, chunk);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Generate a chunk at the specified location.
	 * 
	 * @param rx The relative horizontal location of the Chunk to
	 * 		generate.
	 * @param ry The relative vertical location of the Chunk to
	 * 		generate.
	 */
	public boolean generateChunk(int rx, int ry)
	{
		if (!isChunkAt(rx, ry))
		{
			Chunk chunk = new Chunk(this, rx, ry);
			
			if (!chunks.containsKey(rx))
			{
				chunks.put(rx, new HashMap<Integer, Chunk>());
			}
			
			chunks.get(rx).put(ry, chunk);
			
			chunk.generate();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Generate a chunk at the specified location and perform the hook
	 * after the generation of the Chunk has finished.
	 * 
	 * @param rx The relative horizontal location of the Chunk to
	 * 		generate.
	 * @param ry The relative vertical location of the Chunk to
	 * 		generate.
	 * @param hook The generation hook to perform after the Chunk
	 * 		is generated.
	 */
	public boolean generateChunk(int rx, int ry, Thread hook)
	{
		if (!isChunkAt(rx, ry))
		{
			Chunk chunk = new Chunk(this, rx, ry);
			
			if (!chunks.containsKey(rx))
			{
				chunks.put(rx, new HashMap<Integer, Chunk>());
			}
			
			chunks.get(rx).put(ry, chunk);
			
			chunk.addGenerateHook(hook);
			
			chunk.generate();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Method that generates any Chunks around the proximity of the
	 * specified Actor.
	 * 
	 * @param actor The Actor to generate the Chunks around.
	 */
	public void generateChunksAround(final Actor actor)
	{
		int tileSize    = Tile.getTileSize();
		
		int chunkWidth  = Chunk.getWidth();
		int chunkHeight = Chunk.getHeight();
		
		int width  = Frame.getWidth()  + chunkWidth;
		int height = Frame.getHeight() + chunkHeight;
		
//		width  /= game.getScale();
//		height /= game.getScale();
		
		int startX = -chunkWidth  - width  / 2;
		int startY = -chunkHeight - height / 2;
		
		int x = startX;
		int y = startY;
		
		while (y <= height)
		{
			x = startX;
			
			while (x <= width)
			{
				int offsetX = x + (int)(actor.getX());
				int offsetY = y + (int)(actor.getY());
				
				int rx = offsetX / chunkWidth;
				int ry = offsetY / chunkHeight;
				
				
				if (offsetX < 0)
				{
					rx--;
				}
				if (offsetY < 0)
				{
					ry--;
				}
				
				if (!isChunkAt(rx, ry))
				{
					Chunk chunk = new Chunk(this, rx, ry);
					
					if (!chunks.containsKey(rx))
					{
						chunks.put(rx, new HashMap<Integer, Chunk>());
					}
					
					chunkQueue.enqueue(chunk);
				
					chunks.get(rx).put(ry, chunk);
				}
				
				x += chunkWidth;
			}
			
			y += chunkHeight;
		}
		
		if (!chunkQueue.isEmpty() && !generatingChunks)
		{
			synchronized (generatorThread)
			{
				generatorThread.notify();
			}
		}
	}
	
	/**
	 * Tell whether there is a Chunk already located at the specified
	 * location.
	 * 
	 * @param rx The relative horizontal location of the location
	 * 		to check.
	 * @param ry The relative vertical location of the location
	 * 		to check.
	 * @return Whether there is a Chunk at the location or not.
	 */
	public boolean isChunkAt(int rx, int ry)
	{
		boolean contains = false;
		
		if (chunks.containsKey(rx))
		{
			contains = chunks.get(rx).containsKey(ry);
		}
		
		return contains;
	}
	
	/**
	 * Tell whether there is a Chunk already located at the specified
	 * location relative to the specified Chunk.
	 * 
	 * @param Chunk The Chunk to search relative to.
	 * @param x The relative horizontal location of the location
	 * 		to check.
	 * @param y The relative vertical location of the location
	 * 		to check.
	 * @return Whether there is a Chunk at the location or not.
	 */
	public boolean isChunkAt(Chunk chunk, int x, int y)
	{
		int rx = x / Chunk.CHUNK_SIZE + chunk.getRelativeX();
		int ry = y / Chunk.CHUNK_SIZE + chunk.getRelativeY();
		
		Bounds bounds = trimLocation(x, y, rx, ry);
		
		x  = bounds.getX();
		y  = bounds.getY();
		rx = bounds.getWidth();
		ry = bounds.getHeight();
		
		y %= Chunk.CHUNK_SIZE;
		
		return isChunkAt(rx, ry);
	}
	
	/**
	 * Get the Chunk instance that is located at the specified location
	 * relative to the specified Chunk if there is one.
	 * 
	 * @param Chunk The Chunk to search relative to.
	 * @param x The relative horizontal location of the location
	 * 		to get the Chunk.
	 * @param y The relative vertical location of the location
	 * 		to get the Chunk.
	 * @return The Chunk instance that is located at the specified
	 * 		location relative to the specified Chunk if there is one.
	 */
	public Chunk getChunkAt(int x, int y)
	{
		int rx = x / Chunk.CHUNK_SIZE;
		int ry = y / Chunk.CHUNK_SIZE;
		
		Bounds bounds = trimLocation(x, y, rx, ry);
		
		x  = bounds.getX();
		y  = bounds.getY();
		rx = bounds.getWidth();
		ry = bounds.getHeight();
		
		y %= Chunk.CHUNK_SIZE;
		
		if (isChunkAt(rx, ry))
		{
			return getChunk(rx, ry);
		}
		
		return null;
	}
	
	/**
	 * Get the Chunk instance that is located at the specified location
	 * relative to the specified Chunk if there is one.
	 * 
	 * @param Chunk The Chunk to search relative to.
	 * @param x The relative horizontal location of the location
	 * 		to get the Chunk.
	 * @param y The relative vertical location of the location
	 * 		to get the Chunk.
	 * @return The Chunk instance that is located at the specified
	 * 		location relative to the specified Chunk if there is one.
	 */
	public Chunk getChunkAt(Chunk chunk, int x, int y)
	{
		int rx = x / Chunk.CHUNK_SIZE + chunk.getRelativeX();
		int ry = y / Chunk.CHUNK_SIZE + chunk.getRelativeY();
		
		Bounds bounds = trimLocation(x, y, rx, ry);
		
		x  = bounds.getX();
		y  = bounds.getY();
		rx = bounds.getWidth();
		ry = bounds.getHeight();
		
		if (isChunkAt(rx, ry))
		{
			return getChunk(rx, ry);
		}
		
		return null;
	}
	
	/**
	 * Get the Chunk at the specified relative location.
	 * 
	 * @param rx The relative horizontal location of the Chunk.
	 * @param ry The relative vertical location of the Chunk.
	 * @return The Chunk at the specified relative location.
	 */
	public Chunk getChunk(int rx, int ry)
	{
		if (chunks.containsKey(rx))
		{
			return chunks.get(rx).get(ry);
		}
		
		return null;
	}
	
	/**
	 * Render all of the Chunks in the Map.
	 */
	public void render()
	{
		GL.pushMatrix();
		{
			GL.translate(x, y, 0);
			
			iterateChunks(new Task()
			{
				public void run(Chunk chunk)
				{
					chunk.render();
				}
			});
		
			for (int i = actors.size() - 1; i >= 0; i--)
			{
				Actor actor = actors.get(i);
				
				actor.render();
			}
			
			iterateChunks(new Task()
			{
				public void run(Chunk chunk)
				{
					chunk.renderLighting();
				}
			});
		}
		GL.popMatrix();
	}
	
	/**
	 * Get the Tile located in the Map relative to the Chunk and location
	 * given.
	 * 
	 * @param chunk The Chunk to search relative to.
	 * @param x The horizontal location to search relative to.
	 * @param y The vertical location to search relative to.
	 * @return The Tile at the location.
	 */
	public Tile getTile(Chunk chunk, int x, int y, int layer)
	{
		Tile tile = null;
		
		int rx = chunk.getRelativeX();
		int ry = chunk.getRelativeY();
		
		Bounds bounds = trimLocation(x, y, rx, ry);
		
		x  = bounds.getX();
		y  = bounds.getY();
		rx = bounds.getWidth();
		ry = bounds.getHeight();
		
		if (isChunkAt(rx, ry))
		{
			chunk = chunks.get(rx).get(ry);
			
			tile  = chunk.getTile(x, y, layer);
		}
		
		return tile;
	}
	
	/**
	 * Adds a Tile from the Chunk at the specified location. Still
	 * needs an update() call afterwards.
	 * 
	 * @param tile The tile to add to the queue.
	 * @param x The horizontal location of the new Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @param y The vertical location of the new Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @return Whether a Tile was successfully added or not.
	 */
	public boolean addTile(Tile tile, int x, int y, int layer, boolean replace)
	{
		int chunkSize = Chunk.CHUNK_SIZE;
		
		int rx = 0;
		int ry = 0;
		
		Bounds bounds = trimLocation(x, y, rx, ry);
		
		x  = bounds.getX();
		y  = bounds.getY();
		rx = bounds.getWidth();
		ry = bounds.getHeight();
		
		if (isChunkAt(rx, ry))
		{
			Chunk chunk = chunks.get(rx).get(ry);
			
			return chunk.addTile(tile, x % chunkSize, y % chunkSize, layer, replace);
		}
		
		return false;
	}

	/**
	 * Removes a Tile from the Chunk at the specified location. Still
	 * needs an update() call afterwards.
	 * 
	 * @param x The horizontal location of the Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @param y The vertical location of the Tile.
	 * 		(0 - CHUNK_SIZE-1)
	 * @return Whether a Tile was successfully removed or not.
	 */
	public boolean removeTile(int x, int y, int layer)
	{
		int chunkSize = Chunk.CHUNK_SIZE;
		
		int rx = 0;
		int ry = 0;
		
		Bounds bounds = trimLocation(x, y, rx, ry);
		
		x  = bounds.getX();
		y  = bounds.getY();
		rx = bounds.getWidth();
		ry = bounds.getHeight();
		
		if (isChunkAt(rx, ry))
		{
			Chunk chunk = chunks.get(rx).get(ry);
			
			return chunk.removeTile(x % chunkSize, y % chunkSize, layer);
		}
		
		return false;
	}
	
	/**
	 * Updates the buffers of the Chunk at the specified location.
	 * 
	 * @param x The absolute Tile horizontal location.
	 * @param y The absolute Tile vertical location.
	 */
	public void updateChunkAt(int x, int y)
	{
		int rx = 0;
		int ry = 0;
		
		Bounds bounds = trimLocation(x, y, rx, ry);
		
		x  = bounds.getX();
		y  = bounds.getY();
		rx = bounds.getWidth();
		ry = bounds.getHeight();
		
		if (isChunkAt(rx, ry))
		{
			Chunk chunk = chunks.get(rx).get(ry);
			
			chunk.update();
		}
	}
	
	/**
	 * Trim the specified location to the correct relative location
	 * and local location.
	 * 
	 * @param x The local horizontal location relative to the relative
	 * 		location (tile-wise).
	 * @param y The local vertical location relative to the relative
	 * 		location (tile-wise).
	 * @param rx The relative horizontal location of the Chunk
	 * 		(Chunk-wise).
	 * @param ry The relative vertical location of the Chunk
	 * 		(Chunk-wise).
	 * @return A Bounds instance describing the trimmed (x, y, rx, ry)
	 * 		values.
	 */
	public Bounds trimLocation(int x, int y, int rx, int ry)
	{
		int cs = Chunk.CHUNK_SIZE;
		
		if (x < 0)
		{
			rx -= (-x / cs) + 1;
			
			x = cs - (-x % cs);
			
			if (x == 32)
			{
				x = 0;
				
				rx++;
			}
		}
		else
		{
			rx += x / cs;
			
			x %= cs;
		}
		
		if (y < 0)
		{
			ry -= (-y / cs) + 1;
			
			y = cs - (-y % cs);
			
			if (y == 32)
			{
				y = 0;
				
				ry++;
			}
		}
		else
		{
			ry += y / cs;
			
			y %= cs;
		}
		
		
		return new Bounds(x, y, rx, ry);
	}
	
//	/**
//	 * Calculate the correct relative location given the information.
//	 * 
//	 * @param x The Tile-wise horizontal location.
//	 * @param y The Tile-wise vertical location.
//	 * @param rx The Chunk-wise horizontal location.
//	 * @param ry The Chunk-wise vertical location.
//	 * @return The correct relative locations and offsets.
//	 */
//	public Bounds checkNegativeLocation(int x, int y, int rx, int ry)
//	{
//		int chunkSize = Chunk.CHUNK_SIZE;
//		
//		if (x < 0)
//		{
//			rx--;
//			
//			x = -rx * chunkSize + x;
//			
//			if (x % chunkSize == 0)
//			{
//				rx++;
//				x = 0;
//			}
//		}
//		if (y < 0)
//		{
//			ry--;
//			
//			y = -ry * chunkSize + y;
//			
//			if (y % chunkSize == 0)
//			{
//				ry++;
//				y = 0;
//			}
//		}
//		
//		return new Bounds(x, y, rx, ry);
//	}
	
	/**
	 * Method that uses the iterates through each of the Chunks and
	 * calls the Task.run(Chunk) method for each Chunk.
	 * 
	 * @param task The Task to iterate with.
	 */
	private void iterateChunks(Task task)
	{
		Collection<HashMap<Integer, Chunk>> values = chunks.values();
		
		Iterator iterator = values.iterator();
		
		while (iterator.hasNext())
		{
			Object next = iterator.next();
			
			HashMap<Integer, Chunk> map = (HashMap<Integer, Chunk>)next;
			
			Chunk chunks[] = map.values().toArray(new Chunk[0]);
			
			for (Chunk chunk : chunks)
			{
				task.run(chunk);
			}
		}
	}
	
	/**
	 * Checks whether there is a collision with the Actor and any of the
	 * Chunks.
	 * 
	 * @param actor The Actor to check collisions on.
	 * @return Whether there is a collision.
	 */
	public boolean isCollision(final Actor actor)
	{
		collision = false;
		
		iterateChunks(new Task()
		{
			public void run(Chunk chunk)
			{
				if (chunk.isCollision(actor))
				{
					collision = true;
				}
			}
		});
		
		return collision;
	}
	
	/**
	 * @return The horizontal location of the Map.
	 */
	public float getX()
	{
		return x;
	}
	
	/**
	 * @return The vertical location of the Map.
	 */
	public float getY()
	{
		return y;
	}
	
	/**
	 * Set the absolute location of the Map.
	 * 
	 * @param x The horizontal component to set.
	 * @param y The vertical component to set.
	 */
	public void setLocation(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Move the Map the specified amount.
	 * 
	 * @param dx The displacement horizontal component.
	 * @param dy The displacement vertical component.
	 */
	public void move(float dx, float dy)
	{
		this.x += dx;
		this.y += dy;
	}
	
	/**
	 * Save this Map to the specified world save file.
	 * 
	 * @param world The name to save the world as.
	 */
	public void save(String world)
	{
		final String parent = TheDiggingGame.getResourcesLocation() + "saves/" + world + "/region/";
		
		File parentFile = new File(parent);
		parentFile.mkdirs();
		
		iterateChunks(new Task()
		{
			public void run(Chunk chunk)
			{
				try
				{
					File file = new File(parent + "Chunk" + chunk.getRelativeX() + "," + chunk.getRelativeY() + ".tdg");
					
					file.createNewFile();
					
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
					
					out.writeObject(chunk.getTiles());
					
					out.close();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Load the specified world as the Map.
	 * 
	 * @param world The world save name to load.
	 */
	public void load(String world)
	{
		final String parent = TheDiggingGame.getResourcesLocation() + "saves/" + world + "/region/";
		
		File parentFile = new File(parent);
		
		if (parentFile.isDirectory())
		{
			File children[] = parentFile.listFiles();
			
			for (int i = children.length - 1; i >= 0; i--)
			{
				String fileName = children[i].getName();
				
				String name = fileName.substring(5, fileName.length() - 4);
				
				String loc[] = name.split(",");
				
				int rx = Integer.valueOf(loc[0]);
				int ry = Integer.valueOf(loc[1]);
				
				try
				{
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(children[i]));
					
					Tile tiles[] = (Tile[])in.readObject();
					
					in.close();
					
					generateChunk(rx, ry, tiles);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		iterateChunks(new Task()
		{
			public void run(Chunk chunk)
			{
//				chunk.calculateLighting(true);
				//TODO: Oh, and this
				chunk.updateLighting();
			}
		});
	}
	
	/**
	 * Update everything in the Map.
	 */
	public void update()
	{
		if (!chunkQueue.isEmpty())
		{
			return;
		}
		
		iterateChunks(new Task()
		{
			public void run(Chunk chunk)
			{
				chunk.update();
			}
		});
	}
}