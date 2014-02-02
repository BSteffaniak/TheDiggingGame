package net.foxycorndog.thedigginggame.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.URLClassLoader;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.GameStarter;
import net.foxycorndog.jfoxylib.events.KeyEvent;
import net.foxycorndog.jfoxylib.events.KeyListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.input.Keyboard;
import net.foxycorndog.jfoxylib.web.ConnectionException;
import net.foxycorndog.jfoxylib.web.WebPage;
import net.foxycorndog.jfoxylib.openal.Sound;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.util.FileUtils;
import net.foxycorndog.jfoxylib.util.ResourceLocator;
import net.foxycorndog.thedigginggame.launcher.events.DialogMenuEvent;
import net.foxycorndog.thedigginggame.launcher.events.DialogMenuListener;
import net.foxycorndog.thedigginggame.launcher.menu.MainMenu;
import net.foxycorndog.thedigginggame.launcher.menu.DialogMenu;
import net.foxycorndog.thedigginggame.launcher.menu.OptionsMenu;

/**
 * The Launcher class that checks for updates and launches the game.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 11, 2013 at 8:00:20 PM
 * @since	v0.1
 * @version Mar 11, 2013 at 8:00:20 PM
 * @version	v0.1
 */
public class Launcher extends GameStarter
{
	private boolean				playOffline, playOfflineAnswered;
	private boolean				connecting, connectionSuccessful;
	private boolean				playGame;
	private boolean				launcherUpdate, clientUpdate;

	private int					timeOutLength;

	private long				loadStart;
	
	private	String				clientVersion;
	
	private Object				gameInstance;
	
	private Font				font;

	private MainMenu			mainMenu;
	private DialogMenu			updateMenu, playOfflineMenu;

	private DialogMenuListener	dialogMenuListener;

	private Thread				loaderThread;

	private Method				init, loop, render2D, render3D;

//	private GameInterface		gameInterface;
	
	private	static	final	boolean	debug = true;
	
	private	static	final	String	resourcesLocation = locateResources();
	
	public	static	final	String	VERSION		= "0.9";
	public	static	final	String	SERVER_URL	= "http://www.thedigginggame.co.nf/";
	
	private static	final	String locateResources()
	{
		String resLoc = null;
		
		try
		{
			if (debug)
			{
				resLoc = new File("../thedigginggame").getCanonicalPath();
			}
			else
			{
				String classLoc = Launcher.class.getResource("Launcher.class").toString();
				
				boolean jar = classLoc.startsWith("jar:") || classLoc.startsWith("rsrc:");
				
				if (jar)
				{
					resLoc = new File(new File(System.getProperty("java.class.path")).getCanonicalPath()).getParentFile().getCanonicalPath();
				}
				else
				{
					File f = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI());
					
					File parent = f.getParentFile();
					
					resLoc = parent.getCanonicalPath();
				}
				
				resLoc += "/TheDiggingGame/";
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		resLoc = resLoc.replace("\\", "/");
		resLoc = FileUtils.removeEndingSlashes(resLoc) + "/";
		
		ResourceLocator.setNativesLocation(resLoc);
		
		return resLoc;
	}
	
	/**
	 * The main entry point for the launcher. First method ran.
	 * Creates a launcher instance.
	 * 
	 * @param args The command line arguments.
	 */
	public static void main(String args[])
	{
		Launcher launch = new Launcher();
	}
	
	/**
	 * Construct and start the launcher for the game.
	 */
	public Launcher()
	{
		Frame.create(800, 600);
//		Frame.setVSyncEnabled(true);
		Frame.setResizable(true);
//		Frame.setVSyncEnabled(true);
		
		timeOutLength = 15;
		
		connecting = true;
		
		loaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					loadStart = System.currentTimeMillis();
					
					loadGameFiles();
					checkLauncherVersion();
					
					connectionSuccessful = true;
				}
				catch (ConnectionException e)
				{
					connectionSuccessful = false;
				}
				
				connecting = false;
			}
		};
		
		start();
	}
	
	/**
	 * Loads all of the necessary files that are needed to check the
	 * current client version.
	 */
	private void loadGameFiles()
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					String parentDir = null;
					String jarName   = null;
					
					jarName          = "TDG.jar";
					
					if (debug)
					{
						jarName   = "";
						parentDir = "../thedigginggame/bin/";
					}
					else
					{
						jarName   = "";
						parentDir = "thedigginggame/bin/";
					}
					
					URL urls[] = new URL[]
					{
						new File(parentDir + jarName).toURI().toURL()
					};
					
					URLClassLoader loader = new URLClassLoader(urls);
					
					Class clazz = loader.loadClass("net.foxycorndog.thedigginggame.TheDiggingGame");
					
					Constructor<?> constr = clazz.getConstructor();
				
					gameInstance = constr.newInstance();
					
					init     = clazz.getDeclaredMethod("init", new Class[] { boolean.class, String.class });
					render2D = clazz.getDeclaredMethod("render2D", new Class[] {  });
					render3D = clazz.getDeclaredMethod("render3D", new Class[] {  });
					loop     = clazz.getDeclaredMethod("loop", new Class[] {  });

					Method getVersion = clazz.getDeclaredMethod("getVersion", new Class[] {  });
					
					clientVersion = (String)getVersion.invoke(gameInstance, new Object[] {  });
					
					try
					{
						checkClientVersion();
					}
					catch (ConnectionException e)
					{
						connecting           = false;
						connectionSuccessful = false;
					}
				}
				catch (InstantiationException e)
				{
					e.printStackTrace();
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
		}.start();
	}
	
	/**
	 * Checks the version of the game client to the one on the server.
	 */
	public void checkClientVersion()
	{
		String lines[] = WebPage.getOutput(SERVER_URL + "clientVersion.txt");
		
		if (lines[0].compareTo(clientVersion) > 0)
		{
			clientUpdate = true;
		}
	}
	
	/**
	 * Checks the version of this game launcher to the one on the server.
	 */
	public void checkLauncherVersion()
	{
		String lines[] = WebPage.getOutput(SERVER_URL + "launcherVersion.txt");
		
		String version = lines[0];
		
		if (version.compareTo(VERSION) > 0)
		{
			launcherUpdate = true;
		}
	}
	
	/**
	 * Tell the Launcher that it is ready to play the game.
	 */
	public void startGame()
	{
		playGame            = true;
		
		playOfflineAnswered = false;
	}
	
	/**
	 * Returns whether the game will play offline when started or not.
	 * 
	 * @return Whether the game will play offline when started or not.
	 */
	public boolean willPlayOffline()
	{
		return playOffline;
	}
	
	/**
	 * Set whether to play the game offline or online.
	 * 
	 * @param playOffline Whether or not to play the game offline.
	 */
	public void setPlayOffline(boolean playOffline)
	{
		this.playOffline = playOffline;
	}
	
	/**
	 * Quits/exits the Launcher.
	 */
	public void quit()
	{
		System.exit(0);
	}
	
	/**
	 * Update the launcher to the latest version.
	 */
	private void updateLauncher()
	{
		
		
		launcherUpdate = false;
	}
	
	/**
	 * Update the client to the latest version.
	 */
	private void updateClient()
	{
		
		
		clientUpdate = false;
	}
	
	/**
	 * Initialize the data.
	 */
	public void init()
	{
		GL.setTextureScaleMinMethod(GL.LINEAR);
		GL.setTextureScaleMagMethod(GL.NEAREST);
		
		font = Font.getDefaultFont();
		
		mainMenu = new MainMenu(font, this, null);
		
		dialogMenuListener = new DialogMenuListener()
		{
			public void buttonPressed(DialogMenuEvent event)
			{
				boolean yes = event.wasYes();
				
				DialogMenu source = event.getSource();
				
				if (source == updateMenu)
				{
					mainMenu.setVisible(true);
					
					updateMenu.dispose();
					updateMenu = null;
					
					if (launcherUpdate)
					{
						if (yes)
						{
							updateLauncher();
						}
						
						launcherUpdate = false;
					}
					else if (clientUpdate)
					{
						if (yes)
						{
							updateClient();
						}
						
						clientUpdate = false;
					}
				}
				else if (source == playOfflineMenu)
				{
					if (yes)
					{
						playOffline = true;
					}
					else
					{
						mainMenu.setVisible(true);
					}
					
					playOfflineMenu.dispose();
					playOfflineMenu = null;
					
					playOfflineAnswered = true;
				}
			}
		};
		
		loaderThread.start();
	}

	/**
	 * Method that renders using the Ortho method.
	 */
	public void render2D()
	{
		if (updateMenu != null || mainMenu != null || playOfflineMenu != null)
		{
			GL.scale(3, 3, 1);
			
			if (connecting && !playOffline)//loaderThread.isAlive()
			{
				font.render("Connecting...", 0, 0, 3, Font.RIGHT, Font.BOTTOM, null);
			}
			else if (!connectionSuccessful && !connecting && !playOffline)
			{
				font.render("Could not connect.", 0, 0, 3, Font.RIGHT, Font.BOTTOM, null);
			}
			
			if (playOfflineMenu != null)
			{
				playOfflineMenu.render();
			}
			else if (updateMenu != null)
			{
				updateMenu.render();
			}
			else if (mainMenu != null)
			{
				mainMenu.render();
			}
		}
		else
		{
			try
			{
				render2D.invoke(gameInstance, new Object[] {});
			}
			catch (IllegalAccessException e)
			{
				handleError(e);
			}
			catch (IllegalArgumentException e)
			{
				handleError(e);
			}
			catch (InvocationTargetException e)
			{
				handleError(e);
			}
		}
	}

	/**
	 * Method that renders in the 3D mode.
	 */
	public void render3D()
	{
		if (mainMenu != null)
		{
			
		}
		else
		{
			try
			{
				render3D.invoke(gameInstance, new Object[] {});
			}
			catch (IllegalAccessException e)
			{
				handleError(e);
			}
			catch (IllegalArgumentException e)
			{
				handleError(e);
			}
			catch (InvocationTargetException e)
			{
				handleError(e);
			}
		}
	}
	
	/**
	 * Method that is called each time before the render methods.
	 */
	public void update()
	{
		Frame.setTitle(Frame.getFPS() + "");
		
		if (mainMenu != null)
		{
//			connecting = loaderThread.isAlive() || connecting;
			
			if (launcherUpdate)
			{
				playGame = false;
				
				if (updateMenu == null)
				{
					updateMenu = new DialogMenu("An update is ready for this launcher.\nDownload it?", font, null);
					updateMenu.addDialogMenuListener(dialogMenuListener);
					
					mainMenu.setVisible(false);
				}
			}
			
			if (clientUpdate)
			{
				playGame = false;
				
				if (updateMenu == null)
				{
					updateMenu = new DialogMenu("An update is ready for the game client.\nDownload it?", font, null);
					updateMenu.addDialogMenuListener(dialogMenuListener);
					
					mainMenu.setVisible(false);
				}
			}
			
			if (connecting && !playOffline)
			{
				if (playGame)
				{
					mainMenu.getPlayButton().setText("Please Wait...");
				}
				
				long loadTime = System.currentTimeMillis() - loadStart;
				
				if (loadTime >= timeOutLength * 1000)
				{
					//TODO: timout function
				}
			}
			else
			{
				mainMenu.getPlayButton().setText("Play");
				
				if (playGame)
				{
					if (playGame && (connectionSuccessful || playOffline))
					{
//						gameInterface.init(!playOffline, resourcesLocation);
//						
//						gameInterface.startGame();
						try
						{
							init.invoke(gameInstance, new Object[] { !playOffline, resourcesLocation });
						}
						catch (IllegalAccessException e)
						{
							handleError(e);
						}
						catch (IllegalArgumentException e)
						{
							handleError(e);
						}
						catch (InvocationTargetException e)
						{
							handleError(e);
						}
						
//						System.exit(1);
						
						mainMenu.dispose();
					
						mainMenu = null;
						
						playGame = false;
					}
					else if (playOfflineMenu == null && !playOfflineAnswered)
					{
						playOfflineMenu = new DialogMenu("Could not connect to the server.\nWould you like to play offline?", font, null);
						playOfflineMenu.addDialogMenuListener(dialogMenuListener);
						
						mainMenu.setVisible(false);
					}
				}
			}
		}
		else
		{
			try
			{
				loop.invoke(gameInstance, new Object[] {});
			}
			catch (IllegalAccessException e)
			{
				handleError(e);
			}
			catch (IllegalArgumentException e)
			{
				handleError(e);
			}
			catch (InvocationTargetException e)
			{
				handleError(e);
			}
			
//			try
//			{
//				loop.invoke(obj, null);
//			}
//			catch (IllegalAccessException e)
//			{
//				e.printStackTrace();
//			}
//			catch (IllegalArgumentException e)
//			{
//				e.printStackTrace();
//			}
//			catch (InvocationTargetException e)
//			{
//				e.printStackTrace();
//			}
		}
	}
	
	private void handleError(Exception e)
	{
		e.getCause().printStackTrace();
		
		System.exit(1);
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
}