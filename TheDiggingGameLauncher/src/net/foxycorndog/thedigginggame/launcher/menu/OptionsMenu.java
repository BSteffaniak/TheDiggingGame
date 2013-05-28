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
import net.foxycorndog.thedigginggame.launcher.Launcher;

/**
 * Menu used to edit the preferences for the game.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 14, 2013 at 8:28:22 PM
 * @since	v0.1
 * @version Mar 14, 2013 at 8:28:22 PM
 * @version	v0.1
 */
public class OptionsMenu extends Menu
{
	private Image				backgroundImage;
	
	private Button				playOfflineButton, videoButton, backButton;
	
	private Font				font;
	
	private VideoOptionsMenu	videoOptionsMenu;
	
	private Launcher			launcher;
	
	/**
	 * Create an OptionsMenu.
	 * 
	 * @param parent The parent Panel of this OptionsMenu.
	 */
	public OptionsMenu(final Launcher launcher, Font font, Panel parent)
	{
		super(parent);
		
		this.launcher = launcher;
		
		this.font     = font;
		
		ButtonListener listener = new ButtonListener()
		{
			public void buttonUnHovered(ButtonEvent event)
			{
				
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				if (event.getSource() == playOfflineButton)
				{
					boolean playOffline = launcher.willPlayOffline();
					
					launcher.setPlayOffline(!playOffline);
					
					if (playOffline)
					{
						playOfflineButton.setText("Playing: Online");
					}
					else
					{
						playOfflineButton.setText("Playing: Offline");
					}
				}
				else if (event.getSource() == videoButton)
				{
					openVideoOptionsMenu();
				}
				else if (event.getSource() == backButton)
				{
					launcher.closeOptionsMenu();
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				
			}
		};
		
		playOfflineButton = new Button(this);
		playOfflineButton.setImage(getLargeButtonImage());
		playOfflineButton.setHoverImage(getLargeButtonHoverImage());
		playOfflineButton.setFont(font);
		playOfflineButton.setText(launcher.willPlayOffline() ? "Playing: Offline" : "Playing: Online");
		playOfflineButton.setAlignment(Component.CENTER, Component.CENTER);
		playOfflineButton.setLocation(0, -7);
		playOfflineButton.addButtonListener(listener);
		
		videoButton = new Button(this);
		videoButton.setImage(getLargeButtonImage());
		videoButton.setHoverImage(getLargeButtonHoverImage());
		videoButton.setFont(font);
		videoButton.setText("Video");
		videoButton.setAlignment(Component.CENTER, Component.CENTER);
		videoButton.setLocation(0, -42);
		videoButton.addButtonListener(listener);
		
		backButton = new Button(this);
		backButton.setImage(getLargeButtonImage());
		backButton.setHoverImage(getLargeButtonHoverImage());
		backButton.setFont(font);
		backButton.setText("Back");
		backButton.setAlignment(Component.CENTER, Component.CENTER);
		backButton.setLocation(0, -77);
		backButton.addButtonListener(listener);
		
		int max = Math.max(Display.getWidth(), Display.getHeight());
		
		backgroundImage = new Image(this);
		backgroundImage.setSize(max, max);
		backgroundImage.setImage(getBackgroundImage(), 75, 75);
	}
	
	/**
	 * Method that is called each time it is needed to be rendered.
	 * All of the drawing should be done in this method.
	 */
	public void render()
	{
		super.render();
		
		if (videoOptionsMenu != null)
		{
			videoOptionsMenu.render();
		}
	}
	
	/**
	 * Opens the video options menu so you can edit the video options of
	 * the game.
	 */
	public void openVideoOptionsMenu()
	{
		videoOptionsMenu = new VideoOptionsMenu(this, font, getParent());
		
		setVisible(false);
	}
	
	/**
	 * Closes the video options menu and returns you to this menu.
	 */
	public void closeVideoOptionsMenu()
	{
		videoOptionsMenu.dispose();
		videoOptionsMenu = null;
		
		setVisible(true);
	}
}