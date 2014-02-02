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

import net.foxycorndog.jbiscuit.item.JItem;
import net.foxycorndog.jbiscuit.item.JItemContainer;
import net.foxycorndog.jbiscuit.item.tile.JTile;
import net.foxycorndog.jbiscuit.item.tile.JTileContainer;
import net.foxycorndog.jfoxylib.Display;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.GameStarter;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Component;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.events.FrameEvent;
import net.foxycorndog.jfoxylib.events.FrameListener;
import net.foxycorndog.jfoxylib.events.KeyEvent;
import net.foxycorndog.jfoxylib.events.KeyListener;
import net.foxycorndog.jfoxylib.events.MouseEvent;
import net.foxycorndog.jfoxylib.events.MouseListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.input.Keyboard;
import net.foxycorndog.jfoxylib.input.Mouse;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.jfoxylib.util.Intersects;
import net.foxycorndog.thedigginggame.actor.Player;
import net.foxycorndog.thedigginggame.chat.ChatBox;
import net.foxycorndog.thedigginggame.chat.Command;
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
 * @since	v0.4
 * @version Feb 22, 2013 at 4:23:04 AM
 * @version	v0.4
 */
public class TheDiggingGame
{
	private boolean				online;
	private boolean				tilePlaced;
	
	private int					fps;
	private int					editing;
	private int					counter;
	private int					oldCursorX, oldCursorY;
	private int					oldEditing;
	
	private float				mapScale, guiScale;
	
	private JItemContainer		itemContainer;
	private JTileContainer		tileContainer;
	
	private Cursor				cursor;
	
	private ChatBox				chatBox;
	
	private Player				player;
	
	private Map					map;
	
	private static String		resourcesLocation;
	
	private static Font			font;
	
	public static final String	VERSION	= "0.4";
	
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
	 * Get the scale in which the Map is rendered.
	 * 
	 * @return The scale in which the Map is rendered.
	 */
	public float getMapScale()
	{
		return mapScale;
	}

	/**
	 * Get the scale in which the GUI is rendered.
	 * 
	 * @return The scale in which the GUI is rendered.
	 */
	public float getGUIScale()
	{
		return guiScale;
	}

	/**
	 * Start the game. Exits the current menu that it is in.
	 */
	public void startGame()
	{
		map = new Map(this, tileContainer);

//		map.load("world");
		map.generateChunk(0, 0, new Thread()
		{
			public void run()
			{
				player.teleport(0, 0);
			}
		});

		player = new Player(map, tileContainer);
		player.setFocused(true);
		player.getInventory().setEnabled(false);
		player.setName("Player");

		map.addActor(player);

		cursor = new Cursor(Tile.getTileSize());

		player.center();

		editing = Chunk.MIDDLEGROUND;

		chatBox = new ChatBox(player, 3);

		GL.setClearColor(0.2f, 0.5f, 0.8f, 1);
	}
	
	/**
	 * Initialize the data.
	 */
	public void init(boolean online, String resourcesLocation)
	{
		this.online = online;
		
		TheDiggingGame.resourcesLocation = resourcesLocation;
		
		mapScale = 2;
		guiScale = 4;
		

		int cols = 16;
		int rows = 16;
		
		SpriteSheet sprites = null;
		
		try
		{
			sprites = new SpriteSheet(TheDiggingGame.getResourcesLocation() + "res/images/texturepacks/16/minecraft/terrain.png", cols, rows);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		itemContainer = new JItemContainer(16, 16);
		itemContainer.setSpriteSheet(sprites);
		
		tileContainer = new JTileContainer(16, 16);
		tileContainer.setSpriteSheet(sprites);
		
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
		
		font = Font.getDefaultFont();
		
//		cursor = new Cursor(Tile.getTileSize());
//		
//		player.center();
//		
//		editing = Chunk.MIDDLEGROUND;
//		
//		GL.setClearColor(0.2f, 0.5f, 0.8f, 1);
		
		KeyListener listener = new KeyListener()
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
					if (!chatBox.isOpen())
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
						else if (code == Keyboard.KEY_TAB)
						{
							player.getInventory().toggleOpen();
						}
						else if (code == Keyboard.KEY_SLASH)
						{
							chatBox.open();
						}
						else if (code >= 2 && code <= 10)
						{
							int index = code - 2;
							
							player.getQuickBar().setSelectedIndex(index);
						}
					}
				}
			}
			
			public void keyDown(KeyEvent event)
			{
				
			}
		};
		
		Frame.addFrameListener(new FrameListener()
		{
			public void frameResized(FrameEvent e)
			{
				chatBox.updateSizes();
			}
		});
		
		startGame();
		
		Keyboard.addKeyListener(listener);
	}
	
	/**
	 * Method that renders using the Ortho method.
	 */
	public void render2D()
	{
		if (player.isFocused())
		{
			player.center();
		}
		
		font.render("Editing: " + editing, 0, 0, 10, 2, Font.LEFT, Font.TOP, null);
		
		GL.pushMatrix();
		{
			GL.scale(mapScale, mapScale, 1);
			map.render();
			
			if (!player.getInventory().isOpen())
			{
				renderCursor();
			}
		}
		GL.popMatrix();
		
		if (player.getInventory().isOpen())
		{
			float x = Frame.getWidth()  / 2 - player.getInventory().getBackgroundImage().getScaledWidth()  / 2;
			float y = Frame.getHeight() / 2 - player.getInventory().getBackgroundImage().getScaledHeight() / 2;
			
			GL.pushMatrix();
			{
				GL.translate(x, y, 19);
				
				player.getInventory().render(true);
			}
			GL.popMatrix();
		}
		else
		{
			player.getQuickBar().render();
		}
		
		chatBox.render();
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
		
		if (!player.getInventory().isOpen())
		{
			if (Mouse.isButtonDown(Mouse.LEFT_MOUSE_BUTTON))
			{
				int cursorX = cursor.getX();
				int cursorY = cursor.getY();
				
				if ((cursorX != oldCursorX || cursorY != oldCursorY) || tilePlaced || editing != oldEditing)
				{
					Tile tile = map.getTile(cursorX, cursorY, editing);
					
					if (map.removeTile(cursorX, cursorY, editing))
					{
						player.getInventory().addItem(tile);
						
						// If the action wasnt right mouse button...
						tilePlaced = false;
							
						oldCursorX = cursorX;
						oldCursorY = cursorY;
						
						oldEditing = editing;
					}
				}
			}
			else if (Mouse.isButtonDown(Mouse.RIGHT_MOUSE_BUTTON))
			{
				int cursorX = cursor.getX();
				int cursorY = cursor.getY();
				
				if ((cursorX != oldCursorX || cursorY != oldCursorY) || !tilePlaced || editing != oldEditing)
				{
					JItem item = player.getQuickBar().getSelectedItem();
					
					if (item instanceof JTile)
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
								player.getInventory().removeItem(player.getQuickBar().getSelectedIndex());
								
								// If the action was right mouse button...
								tilePlaced = true;
						
								oldCursorX = cursorX;
								oldCursorY = cursorY;
								
								oldEditing = editing;
							}
						}
					}
				}
			}
			
			if (!chatBox.isOpen())
			{
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
					player.setClimbing(true);
					player.jump();
				}
				else
				{
					player.setClimbing(false);
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT_SHIFT))
				{
					player.setSprinting(true);
				}
				else
				{
					player.setSprinting(false);
				}
			}
			else
			{
				player.setSprinting(false);
			}
		}
		
		if (Mouse.getDWheel() != 0)
		{
			int   dWheel = Mouse.getDWheel() / 112;
			
			if (Keyboard.isKeyDown(Keyboard.KEY_CONTROL))
			{
				float amount = dWheel < 0 ? 0.95f : 1.05f;
				
				mapScale *= (amount);
			}
			else
			{
				int sign = dWheel < 0 ? -1 : 1;
				
				int index = player.getQuickBar().getSelectedIndex();
				
				player.getQuickBar().setSelectedIndex(index - sign);
			}
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
		
		float mx       = Mouse.getX() / mapScale;
		float my       = Mouse.getY() / mapScale;
		
		mx  = (int)mx / tileSize;
		mx *= tileSize;
		my  = (int)my / tileSize;
		my *= tileSize;
		
		mx += map.getX() % tileSize;
		my += map.getY() % tileSize;
		
		if (Mouse.getX() > mx * mapScale + tileSize)
		{
			mx += tileSize;
		}
		if (Mouse.getY() > my * mapScale + tileSize)
		{
			my += tileSize;
		}
		if (Mouse.getX() < mx * mapScale)
		{
			mx -= tileSize;
		}
		if (Mouse.getY() < my * mapScale)
		{
			my -= tileSize;
		}
		
		GL.setTextureScaleMinMethod(GL.LINEAR);
		GL.setTextureScaleMagMethod(GL.NEAREST);
		
		cursor.render(mx, my, 11);
		
		float x = mx / tileSize;
		float y = my / tileSize;
		
		x -= map.getX() / tileSize;
		y -= map.getY() / tileSize;
		
		int finalX = Math.round(x);
		int finalY = Math.round(y);
		
		cursor.setLocation(finalX, finalY);
	}
	
	/**
	 * Get the default Font instance that is used for the game.
	 * 
	 * @return The default Font instance that is used for the game.
	 */
	public static Font getFont()
	{
		return font;
	}
	
	/**
	 * Get the current version of the client that is being ran.
	 * 
	 * @return The current version in the String format.
	 */
	public static String getVersion()
	{
		return VERSION;
	}
}
