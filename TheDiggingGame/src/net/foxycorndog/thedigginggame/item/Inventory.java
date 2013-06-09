package net.foxycorndog.thedigginggame.item;

import java.io.IOException;
import java.util.ArrayList;

import net.foxycorndog.jfoxylib.components.Button;
import net.foxycorndog.jfoxylib.components.Image;
import net.foxycorndog.jfoxylib.events.ButtonEvent;
import net.foxycorndog.jfoxylib.events.ButtonListener;
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
public class Inventory
{
	private	int				capacity;
	private	int				width, height;
	
	private	float			scale;
	private	float			horizontalMargin, verticalMargin;
	private	float			colOffset;
	
	private	Slot			currentItem;
	
	private	Image			currentItemImage;
	private	Image			backgroundImage;
	
	private	ButtonListener	listener;
	
	private	Bundle			bundle;
	
	private	int				slotCounters[];
	
	private	float			rowOffsets[];
	
	private	Button			buttons[];
	
	private Slot			slots[];
	
	private	Queue<Integer>	slotQueue;
	
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
		this.capacity        = width * height;
		
		this.width           = width;
		this.height          = height;
		
		this.backgroundImage = backgroundImage;
		
		this.slots           = new Slot[capacity];
		
		for (int i = 0; i < capacity; i++)
		{
			slots[i] = new Slot();
		}
		
		this.currentItemImage = new Image(null);
		currentItemImage.setImage(Item.getSprites());
		
		listener = new ButtonListener()
		{
			public void buttonUnHovered(ButtonEvent event)
			{
				
			}
			
			public void buttonReleased(ButtonEvent event)
			{
				Button source = event.getSource();
				
				int    button = event.getButton();
				
				for (int id = 0; id < buttons.length; id++)
				{
					if (source == buttons[id])
					{
						if (button == Mouse.LEFT_MOUSE_BUTTON)
						{
							if (currentItem != null)
							{
								currentItem = addItems(id, currentItem, currentItem.instances);
							}
							else
							{
								if (slots[id].item != null)
								{
									currentItem = slots[id].clone();
									
									removeItems(id, slots[id].instances);
								}
							}
						}
						else if (button == Mouse.RIGHT_MOUSE_BUTTON)
						{
							if (currentItem != null)
							{
								currentItem = addItem(id, currentItem);
							}
							else
							{
								if (slots[id].item != null)
								{
									currentItem = slots[id].clone();
									
									int bef = currentItem.instances;
									int rem = currentItem.instances /= 2;
									
									removeItems(id, slots[id].instances - rem);
									
									currentItem.instances = bef - rem;
								}
							}
						}
						
						if (currentItem != null)
						{
							Item item = currentItem.item;
							
							currentItemImage.setSpriteX(item.getX());
							currentItemImage.setSpriteY(item.getY());
							currentItemImage.setSpriteCols(item.getCols());
							currentItemImage.setSpriteRows(item.getRows());
							currentItemImage.updateTexture();
						}
						
						break;
					}
				}
			}
			
			public void buttonDown(ButtonEvent event)
			{
				Button source = event.getSource();
				
				int    button = event.getButton();
				
				for (int id = 0; id < buttons.length; id++)
				{
					if (source == buttons[id])
					{
						
						
						break;
					}
				}
			}
			
			public void buttonUp(ButtonEvent event)
			{
				
			}
			
			public void buttonPressed(ButtonEvent event)
			{
				
			}
			
			public void buttonHovered(ButtonEvent event)
			{
				
			}
		};
		
		bundle    = new Bundle(capacity * 3 * 2, 2, true, false);
		
		buttons   = new Button[capacity];
		
		slotQueue = new Queue<Integer>();
	}
	
	/**
	 * Load the vertices for each of the Inventory slots.
	 * 
	 * @param scale The scale in which to size the rectangles.
	 * @param horizontalMargin The margin that separates each of the
	 * 		rectangles horizontally.
	 * @param verticalMargin The margin that separates each of the
	 * 		rectangles vertically.
	 * @param colOffset The vertical offset of the vertices.
	 * @param rowOffsets Array that contains values that correspond to
	 * 		how much to move the row up
	 */
	public void loadVertices(float scale, float horizontalMargin, float verticalMargin, float colOffset, float rowOffsets[])
	{
		float tileScale       = scale / 2;
		
		horizontalMargin *= scale;
		verticalMargin   *= scale;
		colOffset        *= scale;
		
		for (int i = 0; i < rowOffsets.length; i++)
		{
			rowOffsets[i] *= scale;
		}
		
		this.scale            = scale;
		this.horizontalMargin = horizontalMargin;
		this.verticalMargin   = verticalMargin;
		this.colOffset        = colOffset;
		this.rowOffsets       = rowOffsets;
		
		bundle.beginEditingVertices();
		{
			float rectSize = Tile.getTileSize() * tileScale;
			
			float yOffset = 0;
			
			for (int i = 0; i < capacity; i++)
			{
				int x = i % width;
				int y = i / width;
				
				if (x == 0)
				{
					yOffset += rowOffsets[y];
				}
				
				float xLoc = (x * (rectSize + horizontalMargin) + colOffset);
				float yLoc = (y * (rectSize + verticalMargin) + yOffset);
				
//				xLoc      *= scale;
//				yLoc      *= scale;
				
				bundle.addVertices(GL.genRectVerts(xLoc, yLoc, rectSize, rectSize));
				
				buttons[i] = new Button(null, bundle, 3 * 2 * 2 * i);
				buttons[i].addButtonListener(listener);
				buttons[i].setSize(Math.round(rectSize), Math.round(rectSize), false);
				buttons[i].setLocation(Math.round(xLoc), Math.round(yLoc), false);
			}
		}
		bundle.endEditingVertices();
		
		backgroundImage.setSize(Math.round(backgroundImage.getWidth() * scale), Math.round(backgroundImage.getHeight() * scale));
	}
	
	/**
	 * Set all of the Slots' Buttons enabled or disabled.
	 * 
	 * @param enabled Whether to enable or disable the Buttons.
	 */
	public void setEnabled(boolean enabled)
	{
		for (int id = 0; id < buttons.length; id++)
		{
			buttons[id].setEnabled(enabled);
		}
	}
	
	/**
	 * Get the Image that appears behind the Inventory.
	 * 
	 * @return The Image that appears behind the Inventory.
	 */
	public Image getBackgroundImage()
	{
		return backgroundImage;
	}
	
	/**
	 * Get the Item instance that is located in the specified slotId.
	 * 
	 * @param slotId The slot ID to get the Item from.
	 * @return The Item instance at the specified Slot ID.
	 */
	public Item getItem(int slotId)
	{
		return slots[slotId].item;
	}
	
	/**
	 * Add the one Item instance from the Slot to the new Slot at the
	 * specified Slot ID.
	 * 
	 * @param id The ID of the Slot to add to.
	 * @param slot The Slot to add to the new Slot ID.
	 * @return A Slot instance describing what is left after adding
	 * 		the two Slots together. If the result is null, the Slots
	 * 		had nothing remaining after the addition.
	 */
	public Slot addItem(int id, Slot slot)
	{
		return addItems(id, slot, 1);
	}
	
	/**
	 * Add the Slot to the new Slot at the specified Slot ID.
	 * 
	 * @param id The ID of the Slot to add to.
	 * @param slot The Slot to add to the new Slot ID.
	 * @param quantity The amount of instances to add to the new Slot
	 * 		from the old Slot.
	 * @return A Slot instance describing what is left after adding
	 * 		the two Slots together. If the result is null, the Slots
	 * 		had nothing remaining after the addition.
	 */
	public Slot addItems(int id, Slot slot, int quantity)
	{
		slotQueue.enqueue(id);
			
		int available = Math.min(quantity, slot.instances);
		
		if (slot.item == slots[id].item)
		{
			int bef = slot.instances;
			
			slots[id].addInstances(available);
			
			if (slots[id].instances > slot.item.getStackSize())
			{
				slots[id].instances = bef;
				
				Slot temp = slots[id];
				
				slots[id] = slot;
				
				if (temp.item == null)
				{
					temp = null;
				}
				
				return temp;
			}
			
			Slot leftOver      = new Slot();
			
			leftOver.item      = slot.item;
			leftOver.instances = bef - available;
			
			if (leftOver.instances <= 0)
			{
				leftOver = null;
			}
			
			return leftOver;
		}
		else
		{
			Slot temp = slot.clone();
			
			if (slots[id].item == null)
			{
				Slot newSlot = new Slot();
				
				newSlot.item      = temp.item;
				newSlot.instances = available;
				
				slots[id] = newSlot;
				
				temp.removeInstances(newSlot.instances);
			}
			else
			{
				temp = slots[id];
				
				slots[id] = slot;
			}
			
			if (temp.item == null)
			{
				temp = null;
			}
			
			return temp;
		}
	}
	
	/**
	 * Add the specified Item to the Inventory if there is space
	 * available for it.
	 * 
	 * @param item The Item to add to the Inventory.
	 * @return Whether or not the Item was successfully added to the
	 * 		Inventory.
	 */
	public boolean addItem(Item item)
	{
		return addItem(item, 1) > 0;
	}
	
	/**
	 * Add the specified Item to the Inventory the specified amount of
	 * times if there is space available for it.
	 * 
	 * @param item The Item to add to the Inventory.
	 * @param quantity The amount of the Item to add to the Inventory.
	 * @return The number of instances of the Item that were added into
	 * 		the Inventory.
	 */
	public int addItem(Item item, int quantity)
	{
		int numLeft = quantity;
		
		for (int id = 0; id < slots.length; id++)
		{
			Slot slot = slots[id];
			
			if (slot.item == item)
			{
				if (slot.hasSpace())
				{
					slotQueue.enqueue(id);
					
					// If the Slot doesnt have space for all that
					// needs to be added, fill it up and keep looking.
					if (!slot.hasSpace(numLeft))
					{
						numLeft -= slot.fill();
					}
					else
					{
						slot.addInstances(numLeft);
						
						return quantity;
					}
				}
			}
		}
		
		// If there are still items to add, as well as empty Slots to
		// fill, then fill the empty Slots as much as it can.
		if (numLeft > 0 && calculateNumEmptySlots() > 0)
		{
			for (int id = 0; id < slots.length; id++)
			{
				Slot slot = slots[id];
				
				if (slot.item == null)
				{
					slot.item = item;
					
					slotQueue.enqueue(id);
					
					// If the Slot doesnt have space for all that
					// needs to be added, fill it up and keep looking.
					if (!slot.hasSpace(numLeft))
					{
						numLeft -= slot.fill();
					}
					else
					{
						slot.addInstances(numLeft);
						
						return quantity;
					}
				}
			}
		}
		
		return quantity - numLeft;
	}
	
	/**
	 * Remove one of the instances of an Item at the specified Slot ID.
	 * 
	 * @param id The ID of the Slot to remove from.
	 * @return Whether anything was removed from the Inventory.
	 */
	public boolean removeItem(int id)
	{
		if (slots[id].instances > 0)
		{
			slots[id].removeInstance();
			
			slotQueue.enqueue(id);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove one of the instances of an Item at the specified Slot ID.
	 * 
	 * @param id The ID of the Slot to remove from.
	 * @param quantity The number of instances to remove from the Slot
	 * 		with the specified ID.
	 * @return Whether anything was removed from the Inventory.
	 */
	public boolean removeItems(int id, int quantity)
	{
		if (slots[id].instances > 0)
		{
			slots[id].removeInstances(quantity);
			
			slotQueue.enqueue(id);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove the specified Item from the Inventory if there are any of
	 * them available.
	 * 
	 * @param item The Item to remove from the Inventory.
	 * @return Whether or not the Item was successfully removed from the
	 * 		Inventory.
	 */
	public boolean removeItem(Item item)
	{
		return removeItem(item, 1) > 0;
	}
	
	/**
	 * Remove the specified Item from the Inventory if there are any of
	 * them available.
	 * 
	 * @param item The Item to remove from the Inventory.
	 * @param quantity The amount of the Item to remove from the
	 * 		Inventory.
	 * @return The number of instances of the Item that were removed from
	 * 		the Inventory.
	 */
	public int removeItem(Item item, int quantity)
	{
		int numLeft = quantity;

		for (int id = 0; id < slots.length; id++)
		{
			Slot slot = slots[id];
			
			if (slot.item == item)
			{
				slotQueue.enqueue(id);
				
				if (slot.instances > numLeft)
				{
					slot.removeInstances(numLeft);
					
					return quantity;
				}
				else
				{
					numLeft -= slot.empty();
				}
			}
		}
		
		return quantity - numLeft;
	}
	
	/**
	 * Calculate the number of empty Slots there are in the Inventory.
	 * 
	 * @return The number of empty Slots there are in the Inventory.
	 */
	public int calculateNumEmptySlots()
	{
		int num = 0;
		
		for (int id = 0; id < slots.length; id++)
		{
			Slot slot = slots[id];
			
			if (slot.item == null)
			{
				num++;
			}
		}
		
		return num;
	}
	
	/**
	 * Get whether the Inventory contains the specified Item.
	 * 
	 * @param item The Item to search for.
	 * @return Whether the Inventory contains the specified Item.
	 */
	public boolean contains(Item item)
	{
		for (int id = 0; id < slots.length; id++)
		{
			Slot slot = slots[id];
			
			if (slot.item == item)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get how many of the specified Item is listed in the Inventory.
	 * 
	 * @param item The Item to search for.
	 * @return How many of the specified Item is listed in the Inventory.
	 */
	public int numberOfInstances(Item item)
	{
		int num = 0;
		
		for (int id = 0; id < slots.length; id++)
		{
			Slot slot = slots[id];
			
			Item i = slot.item;
			
			if (i == item)
			{
				num += slot.instances;
			}
		}
		
		return num;
	}
	
	/**
	 * Get the number of slots for holding stacks of Items that the
	 * Inventory has.
	 * 
	 * @return The number of slots for holding stacks of Items that
	 * 		the Inventory has.
	 */
	public int getCapacity()
	{
		return capacity;
	}
	
	/**
	 * Render all of the Slots to the screen.
	 */
	public void render()
	{
		render(false);
	}
	
	/**
	 * Render all of the Slots to the screen.
	 * 
	 * @param renderImage Whether or not to render the Background Image
	 * 		of the Inventory too.
	 */
	public void render(boolean renderImage)
	{
		render(0, capacity, renderImage);
	}
	
	/**
	 * Render the specified amount of Slots to the Display starting
	 * with the specified start ID.
	 * 
	 * @param startId The Slot ID to start the rendering with.
	 * @param amount The amount of slots to render.
	 */
	public void render(int startId, int amount)
	{
		render(startId, amount, false);
	}
	
	/**
	 * Render the specified amount of Slots to the Display starting
	 * with the specified start ID.
	 * 
	 * @param startId The Slot ID to start the rendering with.
	 * @param amount The amount of slots to render.
	 * @param renderImage Whether or not to render the Background Image
	 * 		of the Inventory too.
	 */
	public void render(int startId, int amount, boolean renderImage)
	{
		if (renderImage)
		{
			backgroundImage.render();
		}
		
		for (int id = 0; id < buttons.length; id++)
		{
//			for (int i = 0; i < 100; i++)
			buttons[id].update();
			
//			if (id >= startId && id < startId + amount)
//			{
//				buttons[id].setEnabled(true);
//			}
//			else
//			{
//				buttons[id].setEnabled(false);
//			}
		}
		
		bundle.render(GL.TRIANGLES, startId * 3 * 2, amount * 3 * 2, Item.getSprites());

		float fontScale = scale / 2;
		
		float rectSize  = Tile.getTileSize() * (scale / 2);
		
		Font  font      = TheDiggingGame.getFont();
		
		float yOffset   = 0;
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (x == 0)
				{
					yOffset += rowOffsets[y];
				}
				
				float xLoc = (x * (rectSize + horizontalMargin) + colOffset);
				float yLoc = (y * (rectSize + verticalMargin) + yOffset);
				
				Slot slot = slots[x + y * width];
				
				if (slot.instances > 1)
				{
					String num   = slot.instances + "";
					
					int    width = font.getWidth(num);
					
					float  fontX = xLoc + rectSize - (width * fontScale) / 2;
					float  fontY = yLoc - font.getGlyphHeight() / 2;
					
					GL.setColor(0, 0, 0, 1);
					font.render(num, fontX + 1, fontY - 1, 0, fontScale, null);
					
					GL.setColor(1, 1, 1, 1);
					font.render(num, fontX, fontY, 0, fontScale, null);
				}
			}
		}
		
		if (currentItem != null)
		{
			GL.pushMatrix();
			{
				float scale = this.scale / 2;
				
				GL.resetMatrix();
				GL.translate(0, 0, 20);
				
				int x = Math.round((Mouse.getX() / scale) - rectSize / scale / 2);
				int y = Math.round((Mouse.getY() / scale) - rectSize / scale / 2);
				
				currentItemImage.setLocation(x, y);
				
				GL.pushMatrix();
				{
					GL.scale(scale, scale, 1);
					
					currentItemImage.render();
				}
				GL.popMatrix();
				
				x *= scale;
				y *= scale;
				
				if (currentItem.instances > 1)
				{
					String num   = currentItem.instances + "";
					
					int    width = font.getWidth(num);
					
					float  fontX = x + rectSize - (width * fontScale) / 2;
					float  fontY = y - font.getGlyphHeight() / 2;
					
					GL.setColor(0, 0, 0, 1);
					font.render(num, fontX + 1, fontY - 1, 0, fontScale, null);
					
					GL.setColor(1, 1, 1, 1);
					font.render(num, fontX, fontY, 0, fontScale, null);
				}
			}
			GL.popMatrix();
		}
	}
	
	/**
	 * Update the Buffer for the Inventory to make the Textures up to
	 * date.
	 */
	public void update()
	{
		if (slotQueue.isEmpty())
		{
			return;
		}
		
		bundle.beginEditingTextures();
		{
			while (!slotQueue.isEmpty())
			{
				int    id         = slotQueue.dequeue();
				
				Button button     = buttons[id];
				
				float  textures[] = null;
				
				if (slots[id].instances <= 0)
				{
					textures = new float[3 * 2 * 2];
					
					button.disposeImage(false);
				}
				else
				{
					Item  item      = slots[id].item;
					
					button.setImage(Item.getSprites(), false);
					
					float offsets[] = Item.getSprites().getImageOffsets(item.getX(), item.getY(), item.getCols(), item.getRows());
					textures        = GL.genRectTextures(offsets);
					
					button.setSpriteX(item.getX());
					button.setSpriteY(item.getY());
					button.setSpriteCols(item.getCols());
					button.setSpriteRows(item.getRows());
				
					button.updateTexture(false);
				}
				
//				bundle.setTextures(id * 3 * 2 * 2, textures);
			}
		}
		bundle.endEditingTextures();
	}
	
	/**
	 * Dispose of the Inventory, including the Buttons.
	 */
	public void dispose()
	{
		for (Button button : buttons)
		{
			button.dispose();
		}
	}
	
	/**
	 * Create a String representation of the Inventory instance. Only
	 * includes the Slots that have anything in them.
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * @return The String representation of the Inventory.
	 */
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for (int id = 0; id < slots.length; id++)
		{
			Slot slot = slots[id];
			
			if (slot.instances > 0)
			{
				builder.append(id + ": (").append(slot).append("), ");
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * Class used to organize the slots in an Inventory instance. Each
	 * Slot contains space for the type of Item that fills the Slot,
	 * as well as how many instances of the Item are in the specific
	 * Slot.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Jun 5, 2013 at 2:50:37 PM
	 * @since	v0.3
	 * @version	Jun 5, 2013 at 2:50:37 PM
	 * @version	v0.3
	 */
	private class Slot
	{
		private	int		instances;
		
		private	Item	item;
		
		/**
		 * Increments the number of instances that the Slot has of the
		 * Item that it is holding by one.
		 */
		public void addInstance()
		{
			instances++;
		}
		
		/**
		 * Increments the number of instances that the Slot has of the
		 * Item that it is holding by the specified amount.
		 * 
		 * @param num The amount of times to increment the number of
		 * 		instances.
		 */
		public void addInstances(int num)
		{
			instances += num;
		}
		
		/**
		 * Decrements the number of instances that the Slot has of the
		 * Item that it is holding by one.
		 */
		public void removeInstance()
		{
			instances--;
			
			if (instances <= 0)
			{
				empty();
			}
		}
		
		/**
		 * Decrements the number of instances that the Slot has of the
		 * Item that it is holding by the specified amount.
		 * 
		 * @param num The amount of times to decrement the number of
		 * 		instances.
		 */
		public void removeInstances(int num)
		{
			instances -= num;
			
			if (instances <= 0)
			{
				empty();
			}
		}
		
		/**
		 * Fill the slot with the current Item that it is holding.
		 * 
		 * @return The number of instances that were added to the Slot.
		 */
		public int fill()
		{
			int numAdded = item.getStackSize() - instances;
			
			instances = item.getStackSize();
			
			return numAdded;
		}
		
		/**
		 * Empty out the Slot instance of the Item that it is currently
		 * holding, if any.
		 * 
		 * @return The number of instances that were removed from the
		 * 		Slot.
		 */
		public int empty()
		{
			int numRemoved = instances;
			
			instances = 0;
			item      = null;
			
			return numRemoved;
		}
		
		/**
		 * Get whether the Slot has space for any more instances.
		 * 
		 * @return Whether the Slot has space for any more instances.
		 */
		public boolean hasSpace()
		{
			return instances < item.getStackSize();
		}
		
		/**
		 * Get whether the Slot has space for the specified amount more
		 * of instances.
		 * 
		 * @return Whether the Slot has space for specified amount more
		 * 		of instances.
		 */
		public boolean hasSpace(int num)
		{
			return instances + num <= item.getStackSize();
		}
		
		/**
		 * Return a clone Object of this Slot.
		 * 
		 * @see java.lang.Object#clone()
		 */
		public Slot clone()
		{
			Slot newSlot = new Slot();
			
			newSlot.instances = instances;
			newSlot.item = item;
			
			return newSlot;
		}
		
		/**
		 * Get a String representation of the Slot. The String contains
		 * the Item's String representation, as well as the number of
		 * instances of the Item there are in the Slot.
		 * 
		 * @see java.lang.Object#toString()
		 * 
		 * @return The String representation of the Slot.
		 */
		public String toString()
		{
			return item + ", " + instances;
		}
	}
}