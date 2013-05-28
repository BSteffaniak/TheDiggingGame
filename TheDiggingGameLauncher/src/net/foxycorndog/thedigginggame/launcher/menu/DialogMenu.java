package net.foxycorndog.thedigginggame.launcher.menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import net.foxycorndog.thedigginggame.launcher.events.DialogMenuEvent;
import net.foxycorndog.thedigginggame.launcher.events.DialogMenuListener;

/**
 * Class that is used for a menu that has a yes answer or no answer.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 14, 2013 at 7:31:47 PM
 * @since	v0.1
 * @version Mar 14, 2013 at 7:31:47 PM
 * @version	v0.1
 */
public class DialogMenu extends Menu
{
	private String	text;
	
	private Image	backgroundImage;
	
	private Button	yesButton, noButton;
	
	private Font	font;
	
	private ArrayList<DialogMenuListener> listeners;
	
	/**
	 * Create a DialogMenu that will ask a question as specified by the
	 * String param text.
	 * 
	 * @param text The question to ask on the Menu.
	 * @param font The Font to render the text in.
	 * @param parent The parent of this Menu Panel.
	 */
	public DialogMenu(String text, Font font, Panel parent)
	{
		super(parent);
		
		this.text = text;
		
		this.font = font;
		
		listeners = new ArrayList<DialogMenuListener>();
		
		BufferedImage smallButtonImage      = null;
		BufferedImage smallButtonHoverImage = null;
		BufferedImage background            = null;
		
		try
		{
			smallButtonImage      = ImageIO.read(new File("res/images/GUI/SmallButton.png"));
			smallButtonHoverImage = ImageIO.read(new File("res/images/GUI/SmallButtonHover.png"));
			background            = ImageIO.read(new File("res/images/background.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int max = Math.max(Display.getWidth(), Display.getHeight());
		
		setSize(Frame.getWidth(), Frame.getHeight());
		
		Frame.addFrameListener(new FrameListener()
		{
			public void frameResized(FrameEvent e)
			{
				setSize(Frame.getWidth(), Frame.getHeight());
			}
		});
		
		yesButton = new Button(this);
		yesButton.setImage(smallButtonImage);
		yesButton.setHoverImage(smallButtonHoverImage);
		yesButton.setFont(font);
		yesButton.setText("Yes");
		yesButton.setAlignment(Component.CENTER, Component.CENTER);
		yesButton.setLocation(-50, -10);
		
		noButton = new Button(this);
		noButton.setImage(smallButtonImage);
		noButton.setHoverImage(smallButtonHoverImage);
		noButton.setFont(font);
		noButton.setText("No");
		noButton.setAlignment(Component.CENTER, Component.CENTER);
		noButton.setLocation(50, -10);
		
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
				if (event.getSource() == yesButton)
				{
					notifyListeners(true);
				}
				else if (event.getSource() == noButton)
				{
					notifyListeners(false);
				}
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				
			}
		};
		
		yesButton.addButtonListener(buttonListener);
		noButton.addButtonListener(buttonListener);
	}
	
	/**
	 * Notify all of the listeners that are listening to Button presses.
	 * 
	 * @param yes Whether "yes" was pressed. If not, it was "no".
	 */
	private void notifyListeners(boolean yes)
	{
		for (int i = 0; i < listeners.size(); i++)
		{
			DialogMenuListener listener = listeners.get(i);
			
			DialogMenuEvent event = new DialogMenuEvent(yes, this);
			
			listener.buttonPressed(event);
		}
	}
	
	/**
	 * Add a DialogMenuListener that will be notified whenever a Button
	 * is pressed.
	 * 
	 * @param listener The DialogMenuListener to add.
	 * @return Whether the DialogMenuListener was added successfully.
	 */
	public boolean addDialogMenuListener(DialogMenuListener listener)
	{
		return listeners.add(listener);
	}
	
	/**
	 * Remove a DialogMenuListener from the list of listeners.
	 * 
	 * @param listener The DialogMenuListener to remove.
	 * @return Whether the DialogMenuListener was removed successfully.
	 */
	public boolean removeDialogMenuListener(DialogMenuListener listener)
	{
		return listeners.remove(listener);
	}

	/**
	 * Method that is called each time it is needed to be rendered.
	 * All of the drawing should be done in this method.
	 */
	public void render()
	{
		super.render();
		
		font.render(text, 0, 40, 1, 1, Font.CENTER, Font.CENTER, null);
	}
}