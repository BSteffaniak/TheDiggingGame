package net.foxycorndog.thedigginggame.launcher.menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Component;
import net.foxycorndog.jfoxylib.components.Panel;
import net.foxycorndog.jfoxylib.events.FrameEvent;
import net.foxycorndog.jfoxylib.events.FrameListener;
import net.foxycorndog.jfoxylib.font.Font;

/**
 * Class that is used for each Menu that is used.
 * 
 * @author	Braden Steffaniak
 * @since	Mar 10, 2013 at 12:21:32 AM
 * @since	v0.1
 * @version Mar 10, 2013 at 12:21:32 AM
 * @version	v0.1
 */
public abstract class Menu extends Panel
{
	private ArrayList<Component>	components;
	
	private static BufferedImage	smallButtonImage, smallButtonHoverImage,
			largeButtonImage, largeButtonHoverImage, backgroundImage;
	
	static
	{
		try
		{
			smallButtonImage      = ImageIO.read(new File("res/images/GUI/SmallButton.png"));
			smallButtonHoverImage = ImageIO.read(new File("res/images/GUI/SmallButtonHover.png"));
			largeButtonImage      = ImageIO.read(new File("res/images/GUI/Button.png"));
			largeButtonHoverImage = ImageIO.read(new File("res/images/GUI/ButtonHover.png"));
			backgroundImage            = ImageIO.read(new File("res/images/background.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Construct a Menu that is ready to be customized.
	 */
	public Menu(Panel parent)
	{
		super(parent);
		
		components = new ArrayList<Component>();
		
		setSize(Frame.getWidth(), Frame.getHeight());
		
		Frame.addFrameListener(new FrameListener()
		{
			public void frameResized(FrameEvent e)
			{
				setSize(Frame.getWidth(), Frame.getHeight());
			}
		});
	}
	
	/**
	 * Get the Image that is used to display a small button.
	 * 
	 * @return The Image that is used to display a small button.
	 */
	public BufferedImage getSmallButtonImage()
	{
		return smallButtonImage;
	}
	
	/**
	 * Get the Image that is used to display a small hover button.
	 * 
	 * @return The Image that is used to display a small hover button.
	 */
	public BufferedImage getSmallButtonHoverImage()
	{
		return smallButtonHoverImage;
	}
	
	/**
	 * Get the Image that is used to display a large button.
	 * 
	 * @return The Image that is used to display a large button.
	 */
	public BufferedImage getLargeButtonImage()
	{
		return largeButtonImage;
	}
	
	/**
	 * Get the Image that is used to display a large hover button.
	 * 
	 * @return The Image that is used to display a large hover button.
	 */
	public BufferedImage getLargeButtonHoverImage()
	{
		return largeButtonHoverImage;
	}
	
	/**
	 * Get the Image that is used to display the background image.
	 * 
	 * @return The Image that is used to display the background image.
	 */
	public BufferedImage getBackgroundImage()
	{
		return backgroundImage;
	}
	
//	/**
//	 * Add a Button to the Menu.
//	 * 
//	 * @param button The Button to add to the Menu.
//	 */
//	public void addComponent(Component component)
//	{
//		components.add(component);
//	}
//
//	/**
//	 * Renders the Menu to the screen.
//	 */
//	public void render()
//	{
//		for (int i = components.size() - 1; i >= 0; i--)
//		{
//			Component component = components.get(i);
//			
//			component.render();
//		}
//	}
}