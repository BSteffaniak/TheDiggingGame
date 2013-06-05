package net.foxycorndog.thedigginggame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.imageio.ImageIO;

import net.foxycorndog.jfoxylib.Display;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.GameStarter;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Component;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.events.KeyEvent;
import net.foxycorndog.jfoxylib.events.KeyListener;
import net.foxycorndog.jfoxylib.events.MouseEvent;
import net.foxycorndog.jfoxylib.events.MouseListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.input.Keyboard;
import net.foxycorndog.jfoxylib.input.Mouse;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.util.Intersects;
import net.foxycorndog.thedigginggame.actor.Player;
import net.foxycorndog.thedigginggame.components.Cursor;
import net.foxycorndog.thedigginggame.item.Item;
import net.foxycorndog.thedigginggame.item.tile.Tile;
import net.foxycorndog.thedigginggame.map.Chunk;
import net.foxycorndog.thedigginggame.map.Map;

/**
 * Main class for the Game. Starts the game.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 22, 2013 at 4:23:04 AM
 * @since	v0.1
 * @version Feb 22, 2013 at 4:23:04 AM
 * @version	v0.1
 */
public class TheDiggingGame
{
	private					boolean			online;
	private					boolean			tilePlaced;
	
	private					int				fps;
	private					int				editing;
	private					int				counter;
	private					int				oldCursorX, oldCursorY;
	
	private					float			scale;
	
	private					Cursor			cursor;
	
	private					Player			player;
	
	private					Font			font;
	
	private					Map				map;
	
	private	static			String			resourcesLocation;
	
	public	static	final	String			VERSION	= "0.1";
	
	/**
	 * Main method for the game. First method ran.
	 * 
	 * @param args The command line arguments.
	 */
	public static void main(String args[])
	{
		try
		{
			boolean debug    = true;
			
			String parentDir = null;
			String jarName   = null;
			
			jarName          = "TDGLauncher.jar";
			
			if (debug)
			{
				jarName   = "";
				parentDir = "../thedigginggamelauncher/bin/";
//				parentDir = "../thedigginggame/bin/";
			}
			
			URL urls[] = new URL[]
			{
				new File(parentDir + jarName).toURI().toURL()
			};
			
			URLClassLoader loader = new URLClassLoader(urls);
			
			Class clazz = loader.loadClass("net.foxycorndog.thedigginggame.launcher.Launcher");
			
			clazz.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new String[1]);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor of the game.
	 */
	public TheDiggingGame()
	{
		
	}
	
	/**
	 * Get the location in which the resources for the game are stored.
	 * 
	 * @return The location in which the resources for the game are
	 * 		stored.
	 */
	public static String getResourcesLocation()
	{
		return resourcesLocation;
	}
	
	/**
	 * Initialize the data.
	 */
	public void init(boolean online, String resourcesLocation)
	{
		this.online = online;
		
		this.resourcesLocation = resourcesLocation;
		
		scale = 2;
		
//		map = new Map(this);
//		
//		map.load("world");
////		map.generateChunk(0, 0);
//		
//		player = new Player(map);
//		player.setLocation(16 * 6, 16 * 13);
//		player.setFocused(true);
//		
//		map.addActor(player);
		
		try
		{
			font = new Font("res/images/fonts/font.png", 26, 4,
					new char[]
					{
						'A', 'B', 'C', 'D', 'E', 'F',  'G', 'H', 'I', 'J', 'K', 'L',  'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
						'a', 'b', 'c', 'd', 'e', 'f',  'g', 'h', 'i', 'j', 'k', 'l',  'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
						'0', '1', '2', '3', '4', '5',  '6', '7', '8', '9', '_', '-',  '+', '=', '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
						'?', '>', '<', ';', ':', '\'', '"', '{', '}', '[', ']', '\\', '|', ',', '.', '/', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '
					});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
//		cursor = new Cursor(Tile.getTileSize());
//		
//		player.center();
//		
//		editing = Chunk.MIDDLEGROUND;
//		
//		GL.setClearColor(0.2f, 0.5f, 0.8f, 1);
		
		Keyboard.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent event)
			{
			}
			
			public void keyReleased(KeyEvent event)
			{
			}
			
			public void keyPressed(KeyEvent event)
			{
				int code = event.getKeyCode();
				
				if (map != null)
				{
					if (code == Keyboard.KEY_L)
					{
						map.save("world");
					}
					if (code == Keyboard.KEY_Q)
					{
						editing--;
						
						if (editing < 0)
						{
							editing = Chunk.FOREGROUND;
						}
					}
					else if (code == Keyboard.KEY_E)
					{
						editing = (editing + 1) % 3;
					}
				}
			}
			
			public void keyDown(KeyEvent event)
			{
				
			}
		});
		
		startGame();
	}
	
	/**
	 * Start the game. Exits the current menu that it is in.
	 */
	public void startGame()
	{
		map = new Map(this);
		
//		map.load("world");
		map.generateChunk(0, 0);
		
		player = new Player(map);
		player.setLocation(16 * 6, 16 * 13);
		player.setFocused(true);
		
//		System.out.println("asdf" );
//		System.exit(0);
		map.addActor(player);
		
		cursor = new Cursor(Tile.getTileSize());
		
		player.center();
		
		editing = Chunk.MIDDLEGROUND;
		
		GL.setClearColor(0.2f, 0.5f, 0.8f, 1);
	}
	
	/**
	 * @return The scale in which the game is rendered.
	 */
	public float getScale()
	{
		return scale;
	}
	
	/**
	 * Method that renders using the Ortho method.
	 */
	public void render2D()
	{
		font.render("Editing: " + editing, 0, 0, 10, 2, Font.LEFT, Font.TOP, null);
		
		GL.pushMatrix();
		{
			GL.scale(scale, scale, 1);
			map.render();
			
			renderCursor();
		}
		GL.popMatrix();
		
		player.getQuickBar().render();
	}
	
	/**
	 * Method that renders in the 3D mode.
	 */
	public void render3D()
	{
		
	}
	
	/**
	 * Method that is called each time before the render methods.
	 */
	public void loop()
	{
		if (fps != Frame.getFPS())
		{
			fps = Frame.getFPS();
			
			Frame.setTitle("FPS: " + fps);
		}
		
		int target = Frame.getTargetFPS();
		
		if (target == 0)
		{
			target = 60;
		}
		
		float delta = 60f / (fps == 0 ? target : fps);
		
		if (Mouse.isButtonDown(Mouse.LEFT_MOUSE_BUTTON))
		{
			int cursorX = cursor.getX();
			int cursorY = cursor.getY();
			
			if ((cursorX != oldCursorX || cursorY != oldCursorY) || tilePlaced)
			{
				Tile tile = map.getTile(cursorX, cursorY, editing);
				
				if (map.removeTile(cursorX, cursorY, editing))
				{
					player.getInventory().addItem(tile);
					
					// If the action wasnt right mouse button...
					tilePlaced = false;
						
					oldCursorX = cursorX;
					oldCursorY = cursorY;
				}
			}
		}
		else if (Mouse.isButtonDown(Mouse.RIGHT_MOUSE_BUTTON))
		{
			int cursorX = cursor.getX();
			int cursorY = cursor.getY();
			
			if ((cursorX != oldCursorX || cursorY != oldCursorY) || !tilePlaced)
			{
				Item item = player.getInventory().getItem(0);
				
				if (item instanceof Tile)
				{
					Tile    tile     = (Tile)item;
					
					int     ts       = Tile.getTileSize();
					
					boolean canPlace = editing != Chunk.MIDDLEGROUND || (!tile.isCollidable() ||
							!Intersects.rectangles(cursorX * ts, cursorY * ts, tile.getCols() * ts, tile.getRows() * ts,
									player.getX() + 1, player.getY(), player.getWidth() - 2, player.getHeight() - 3));
					
					if (canPlace)
					{
						if (map.addTile(tile, cursorX, cursorY, editing, false))
						{
							player.getInventory().removeItem(tile);
							
							// If the action was right mouse button...
							tilePlaced = true;
					
							oldCursorX = cursorX;
							oldCursorY = cursorY;
						}
					}
				}
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			player.moveLeft(delta);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			player.moveRight(delta);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			player.jump();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT_SHIFT))
		{
			player.setSprinting(true);
		}
		else
		{
			player.setSprinting(false);
		}
		
		if (Mouse.getDWheel() != 0)
		{
			int   dWheel = Mouse.getDWheel() / 112;
			
			float amount = dWheel < 0 ? 0.95f : 1.05f;
			
			scale *= (amount);
		}
		
		map.generateChunksAround(player);
		
		map.update(delta);
		
		counter++;
		
		counter = counter > 1000000 ? 0 : counter;
	}
	
	/**
	 * Render the Cursor that is used in game for breaking Tiles.
	 */
	private void renderCursor()
	{
		int   tileSize = Tile.getTileSize();
		
		float mx       = Mouse.getX() / scale;
		float my       = Mouse.getY() / scale;
		
		mx  = (int)mx / tileSize;
		mx *= tileSize;
		my  = (int)my / tileSize;
		my *= tileSize;
		
		mx += map.getX() % tileSize;
		my += map.getY() % tileSize;
		
		if (Mouse.getX() > mx * scale + tileSize)
		{
			mx += tileSize;
		}
		if (Mouse.getY() > my * scale + tileSize)
		{
			my += tileSize;
		}
		if (Mouse.getX() < mx * scale)
		{
			mx -= tileSize;
		}
		if (Mouse.getY() < my * scale)
		{
			my -= tileSize;
		}
		
		cursor.render(mx, my, 11);
		
		float x = mx / tileSize;
		float y = my / tileSize;
		
		x -= map.getX() / tileSize;
		y -= map.getY() / tileSize;
		
		int finalX = (int)Math.round(x);
		int finalY = (int)Math.round(y);
		
		cursor.setLocation(finalX, finalY);
	}
	
	/**
	 * Get the current version of the client that is being ran.
	 * 
	 * @return The current version in the String format.
	 */
	public String getVersion()
	{
		return VERSION;
	}
}