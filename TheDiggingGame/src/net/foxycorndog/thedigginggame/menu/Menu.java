package net.foxycorndog.thedigginggame.menu;

import java.util.ArrayList;

import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Component;
import net.foxycorndog.jfoxylib.components.Panel;

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
	private ArrayList<Component> components;
	
	/**
	 * Construct a Menu that is ready to be customized.
	 */
	public Menu(Panel parent)
	{
		super(parent);
		
		components = new ArrayList<Component>();
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