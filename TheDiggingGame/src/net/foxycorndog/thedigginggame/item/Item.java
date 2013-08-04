package net.foxycorndog.thedigginggame.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.foxycorndog.jbiscuit.item.JItem;
import net.foxycorndog.jbiscuit.item.JItemContainer;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.thedigginggame.TheDiggingGame;

/**
 * Class used to describe an Object that can be held in an Inventory
 * and have visual quality in the game.
 * 
 * @author	Braden Steffaniak
 * @since	Jun 2, 2013 at 9:11:11 PM
 * @since	v0.3
 * @version	Jun 2, 2013 at 9:11:11 PM
 * @version	v0.3
 */
public class Item extends JItem
{
	/**
	 * Create an Item instance with the specific properties that guide
	 * how the Item interacts with its surroundings.
	 * 
	 * @param name The name of the Item that is being created.
	 * @param stackSize The amount of specific Item instances can be
	 * 		stacked in one Inventory Slot before filling the Slot to
	 * 		its max.
	 * @param container The JItemContainer to add the Item to.
	 */
	public Item(String name, int x, int y, int cols, int rows, int stackSize, JItemContainer container)
	{
		super(name, x, y, cols, rows, stackSize, container);
	}
}