package net.foxycorndog.thedigginggame.item;

import java.util.ArrayList;

import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxyutil.Queue;
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
	
	private	Bundle			bundle;
	
	private Slot			slots[];
	
	private	Queue<Integer>	slotQueue;
	
	/**
	 * Create an Inventory instance.
	 * 
	 * @param capacity The number of slots for holding stacks of Items
	 * 		that the Inventory has.
	 */
	public Inventory(int capacity)
	{
		this.capacity = capacity;
		
		width         = 9;
		height        = capacity / width;
		
		slots = new Slot[capacity];
		
		for (int i = 0; i < capacity; i++)
		{
			slots[i] = new Slot();
		}
		
		bundle = new Bundle(capacity * 3 * 2, 2, true, false);
		
		loadVertices(3, 16);
		
		slotQueue = new Queue<Integer>();
	}
	
	private void loadVertices(float scale, int margin)
	{
		bundle.beginEditingVertices();
		{
			float rectSize = Tile.getTileSize() * scale;
			
			for (int i = 0; i < capacity; i++)
			{
				int x = i % width;
				int y = i / width;
				
				bundle.addVertices(GL.genRectVerts(x * (rectSize + margin), y * (rectSize), rectSize, rectSize));
			}
		}
		bundle.endEditingVertices();
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
						slot.addInstance(numLeft);
						
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
						slot.addInstance(numLeft);
						
						return quantity;
					}
				}
			}
		}
		
		return quantity - numLeft;
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
					slot.removeInstance(numLeft);
					
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
		render(0, capacity);
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
		bundle.render(GL.TRIANGLES, startId * 3 * 2, amount * 3 * 2, Item.getSprites());
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
				int id = slotQueue.dequeue();
				
				float textures[] = null;
				
				if (slots[id].instances <= 0)
				{
					textures = new float[3 * 2 * 2];
				}
				else
				{
					Item  item      = slots[id].item;
					
					float offsets[] = Item.getSprites().getImageOffsets(item.getX(), item.getY(), item.getCols(), item.getRows());
					
					textures        = GL.genRectTextures(offsets);
				}
				
				bundle.setTextures(id * 3 * 2 * 2, textures);
			}
		}
		bundle.endEditingTextures();
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
		public void addInstance(int num)
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
		}
		
		/**
		 * Decrements the number of instances that the Slot has of the
		 * Item that it is holding by the specified amount.
		 * 
		 * @param num The amount of times to decrement the number of
		 * 		instances.
		 */
		public void removeInstance(int num)
		{
			instances -= num;
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