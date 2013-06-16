package net.foxycorndog.thedigginggame.launcher.menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.foxycorndog.jfoxylib.Display;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Component;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.components.Panel;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.events.FrameEvent;
import net.foxycorndog.jfoxylib.events.FrameListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.util.Bounds2f;
import net.foxycorndog.jfoxylib.util.FileUtils;
import net.foxycorndog.thedigginggame.launcher.Launcher;

/**
 * Class that is used for the Main Menu of the game. Contains
 * a few Buttons that are used as the Main interface for the game.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 10, 2013 at 12:41:56 AM
 * @since	v0.1
 * @version Mar 10, 2013 at 12:41:56 AM
 * @version	v0.1
 */
public class MainMenu extends Menu
{
	private boolean	splashScaleIncreasing;
	private boolean	rUp, gUp, bUp;
	
	private int		r, g, b;
	private int		width, height;
	
	private float	counter;
	private float	splashScale;
	
	private String	splash;
	
	private Image	backgroundImage;
	
	private Button	playButton, optionsButton, quitButton;
	
	private Font	font;
	
	private static String	splashes[];
	
	private static void init()
	{
		if (splashes != null)
		{
			return;
		}
		
		splashes = FileUtils.readFile(Launcher.getResourcesLocation() + "res/splashes.txt");
	}
	
	/**
	 * Construct the Main Menu with the specified parent Panel.
	 * 
	 * @param font The Font to render the text on the Buttons with.
	 * @param parent The parent Panel of this Main Menu.0
	 */
	public MainMenu(Font font, final Launcher launcher, Panel parent)
	{
		super(parent);
		
		init();
		
		this.font = font;

		r = (int)(Math.random() * 256);
		g = (int)(Math.random() * 256);
		b = (int)(Math.random() * 256);
		
		r = r <= 100 ? 100 : r;
		g = g <= 100 ? 100 : g;
		b = b <= 100 ? 100 : b;
		
		splashScale = 1;
		
		splash = splashes[(int)(Math.random() * splashes.length)];
		
		final float strobeSpeed = 0.01f;
		
		new Thread()
		{
			public void run()
			{
				while (!isDisposed())
				{
					if (splashScaleIncreasing)
					{
						splashScale *= 1 + strobeSpeed;
						
						if (splashScale >= 1.13f)
						{
							splashScaleIncreasing = false;
						}
					}
					else
					{
						splashScale *= 1 - strobeSpeed;
						
						if (splashScale <= 0.97f)
						{
							splashScaleIncreasing = true;
						}
					}
					
					try
					{
						Thread.sleep(10);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		BufferedImage normalImage = null;
		BufferedImage hoverImage  = null;
		BufferedImage background  = null;
		
		try
		{
			String res = Launcher.getResourcesLocation();
			
			normalImage = ImageIO.read(new File(res + "res/images/GUI/button.png"));
			hoverImage  = ImageIO.read(new File(res + "res/images/GUI/buttonhover.png"));
			background = ImageIO.read(new File(res + "res/images/background.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		playButton = new Button(this);
		playButton.setAlignment(Component.CENTER, Component.CENTER);
		playButton.setLocation(0, -7);
		playButton.setFont(font);
		playButton.setText("Play");
		playButton.setImage(normalImage);
		playButton.setHoverImage(hoverImage);

		optionsButton = new Button(this);
		optionsButton.setAlignment(Component.CENTER, Component.CENTER);
		optionsButton.setLocation(0, -42);
		optionsButton.setFont(font);
		optionsButton.setText("Options");
		optionsButton.setImage(normalImage);
		optionsButton.setHoverImage(hoverImage);

		quitButton = new Button(this);
		quitButton.setAlignment(Component.CENTER, Component.CENTER);
		quitButton.setLocation(0, -77);
		quitButton.setFont(font);
		quitButton.setText("Quit");
		quitButton.setImage(normalImage);
		quitButton.setHoverImage(hoverImage);
		
		int max = Math.max(Display.getWidth(), Display.getHeight());
		
		backgroundImage = new Image(this);
		backgroundImage.setSize(max, max);
		backgroundImage.setImage(background, 75, 75);
		
		ButtonListener buttonListener = new ButtonListener()
		{
			public void buttonUnHovered(ButtonEvent event)
			{
				
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				if (event.getSource() == playButton)
				{
					launcher.startGame();
				}
				else if (event.getSource() == optionsButton)
				{
					launcher.openOptionsMenu();
				}
				else if (event.getSource() == quitButton)
				{
					dispose();
					
					launcher.quit();
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				
			}

			public void buttonUp(ButtonEvent event)
			{
				
			}

			public void buttonDown(ButtonEvent event)
			{
				
			}
		};

		playButton.addButtonListener(buttonListener);
		optionsButton.addButtonListener(buttonListener);
		quitButton.addButtonListener(buttonListener);
		
		width  = font.getWidth("The Digging");
		height = 80 + font.getGlyphHeight();
	}
	
	/**
	 * Renders the Component to the screen.
	 */
	public void render()
	{
		float delta = 60f / Frame.getFPS();
		
		counter += delta;
		
		super.render();
		
		renderSplashMessage();

		GL.setColor(r / 255f, g / 255f, b / 255f, 1);
		font.render("The Digging", 0, 73, 2, 3, Font.CENTER, Font.CENTER, null);
		font.render("Game", 0, 44, 2, 3, Font.CENTER, Font.CENTER, null);
		
		GL.setColor(0, 0, 0, 0.5f);
		font.render("The Digging", 3, 73 + 3, 1, 3, Font.CENTER, Font.CENTER, null);
		font.render("Game", 3, 44 + 3, 1, 3, Font.CENTER, Font.CENTER, null);
		
		GL.setColor(1, 1, 1, 1);
			
		updateColor();
	}
	
	/**
	 * Renders the yellow subliminal message copied from the original
	 * Minecraft to the screen.
	 */
	private void renderSplashMessage()
	{
		GL.pushMatrix();
		{
			GL.scale(splashScale, splashScale, 1);
			
			float x     = 50;
			float y     = 20;
			float z     = 4;
			float scale = 2;
			
			Bounds2f bounds = font.getRenderBounds(splash, x, y, scale, Font.CENTER, Font.CENTER, null);
			
			float scaled[] = GL.getAmountScaled();
			
			float rx = bounds.getX() / scaled[0];
			float ry = bounds.getY() / scaled[1];
			
			float width  = 0;//bounds.getWidth();
			float height = 0;//bounds.getHeight();
			
			GL.translate(rx + width / 2, ry + height / 2, 10);
			GL.rotate(0, 0, 15);
			GL.translate(-rx - width / 2, -ry - height / 2, 0);
			
			GL.setColor(0, 0, 0, 0.5f);
			font.render(splash, x + 2, y + 2, z - 1, scale, Font.CENTER, Font.CENTER, null);
		
			GL.setColor(1, 1, 0, 1);
			font.render(splash, x, y, z, scale, Font.CENTER, Font.CENTER, null);
		}
		GL.popMatrix();
	}
	
	/**
	 * Updates the color slightly each frame so it looks pretty.
	 */
	private void updateColor()
	{
		if (counter >= 1)
		{
			r = rUp ? r + 1 : r - 1;
			r = r >= 256 ? 255 : r;
			r = r <= 100 ? 100 : r;
			
			g = gUp ? g + 1 : g - 1;
			g = g >= 256 ? 255 : g;
			g = g <= 100 ? 100 : g;
			
			b = bUp ? b + 1 : b - 1;
			b = b >= 256 ? 255 : b;
			b = b <= 100 ? 100 : b;
			
			if ((int)(Math.random() * 100) == 0)
			{
				rUp = !rUp;
			}
			if ((int)(Math.random() * 100) == 0)
			{
				gUp = !gUp;
			}
			if ((int)(Math.random() * 100) == 0)
			{
				bUp = !bUp;
			}
			
			counter = 0;
		}
	}
	
	/**
	 * Get the Play Button instance.
	 * 
	 * @return The Play Button instance.
	 */
	public Button getPlayButton()
	{
		return playButton;
	}
}