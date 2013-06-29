package net.foxycorndog.thedigginggame.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.foxycorndog.jbiscuit.item.JInventory;
import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
import net.foxycorndog.jfoxylib.events.MouseEvent;
import net.foxycorndog.jfoxylib.events.MouseListener;
import net.foxycorndog.jfoxylib.font.Font;
import net.foxycorndog.jfoxylib.input.Mouse;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.jfoxyutil.Queue;
import net.foxycorndog.thedigginggame.TheDiggingGame;
import net.foxycorndog.thedigginggame.item.tile.Tile;

/**
 * Class used to keep track of the Items.
 * 
 * @author	Braden Steffaniak
 * @since	Jun 3, 2013 at 5:12:02 PM
 * @since	v0.3
 * @version	Jun 3, 2013 at 5:12:02 PM
 * @version	v0.3
 */
public class Inventory extends JInventory
{
	public	static	final	Image	PLAYER_INVENTORY_IMAGE, CHEST_INVENTORY_IMAGE;
	
	static
	{
		PLAYER_INVENTORY_IMAGE = new Image(null);
		CHEST_INVENTORY_IMAGE  = new Image(null);
			
		try
		{
			SpriteSheet playerInventorySprites = new SpriteSheet(TheDiggingGame.getResourcesLocation() + "res/images/gui/PlayerInventory.png", 256, 256);
			
			PLAYER_INVENTORY_IMAGE.setSpriteX(6);
			PLAYER_INVENTORY_IMAGE.setSpriteY(32);
			PLAYER_INVENTORY_IMAGE.setSpriteCols(185);
			PLAYER_INVENTORY_IMAGE.setSpriteRows(129);
			PLAYER_INVENTORY_IMAGE.setImage(playerInventorySprites);
			
//			CHEST_INVENTORY_IMAGE.setImage(TheDiggingGame.getResourcesLocation() + "res/images/gui/ChestInventory.png");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Create an Inventory instance.
	 * 
	 * @param capacity The number of slots for holding stacks of Items
	 * 		that the Inventory has.
	 */
	public Inventory(int width, int height, Image backgroundImage)
	{
		super(width, height);
		
		setBackgroundImage(backgroundImage);
	}
}