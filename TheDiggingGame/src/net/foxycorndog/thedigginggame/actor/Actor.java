package net.foxycorndog.thedigginggame.actor;

import java.util.ArrayList;

import net.foxycorndog.jbiscuit.actor.JActor;
import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.jfoxylib.util.Bounds;
import net.foxycorndog.thedigginggame.item.Inventory;
import net.foxycorndog.thedigginggame.item.tile.Tile;
import net.foxycorndog.thedigginggame.map.Chunk;
import net.foxycorndog.thedigginggame.map.Map;

/**
 * Class that keeps the information for any Actor type. Such information
 * includes the location, speed, size, and other states.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 22, 2013 at 4:23:30 AM
 * @since	v0.1
 * @version Feb 22, 2013 at 11:32:08 PM
 * @version	v0.1
 */
public class Actor extends JActor
{
	private	boolean			climbing;
	private	boolean			increaseRot;
	private	boolean			sprinting;
	
	private	int				facing, oldFacing;
	
	private	float			rotation;
	
	/**
	 * Creates an actor with the specified characteristics.
	 * 
	 * @param map The map to add the actor to.
	 * @param width The width of the sprite of the Actor.
	 * @param height The height of the sprite of the Actor.
	 * @param speed The speed of the actor when walking.
	 * @param jumpHeight The height in which the Actor can jump.
	 */
	public Actor(Map map, int width, int height, float speed, float jumpHeight, int maxWalkCycle)
	{
		super(map, width, height, speed, jumpHeight, maxWalkCycle);
	}
	
	/**
	 * Try to climb.
	 * 
	 * @param delta The delta to use when calculating how far to
	 * 		move up.
	 * @return Whether the Actor successfully climbed or not.
	 */
	private boolean tryClimb(float delta)
	{
		Tile tiles[] = ((Map)getMap()).getAdjacentTiles(this, Chunk.MIDDLEGROUND);
		
		float max = 0;
		
		for (Tile tile : tiles)
		{
			if (tile.getClimbSpeed() > max)
			{
				max = tile.getClimbSpeed();
			}
		}
		
		if (max > 0)
		{
			return tryMove(0, max * delta);
		}
		
		return false;
	}
	
	/**
	 * Centers the Actor in the screen.
	 */
	public void center()
	{
		float scale = ((Map)getMap()).getGame().getMapScale();
		
		((Map)getMap()).setLocation(-getX() + ((Frame.getWidth() / 2) / scale) - getWidth() / 2, -getY() + ((Frame.getHeight() / 2) / scale) - getHeight() / 2);
	}
	
	/**
	 * Get the rotation of the Actor.
	 * 
	 * @return The rotation of the Actor.
	 */
	public float getRotation()
	{
		return rotation;
	}
	
	/**
	 * Update the Actor every frame.
	 */
	public void update(float delta)
	{
		super.update(delta);
		
		float spd = 5  * (sprinting ? 1.7f  : 1);
		float max = 55 * (sprinting ? 1.25f : 1);
		
		if (isMovingHorizontally() && oldFacing == facing)
		{
			if (increaseRot)
			{
				rotation += spd * delta;
				
				if (rotation >= max)
				{
					increaseRot = false;
				}
			}
			else
			{
				rotation -= spd * delta;
				
				if (rotation <= -max)
				{
					increaseRot = true;
				}
			}
		}
		else
		{
			rotation = 0;
		}
		
		oldFacing    = facing;
		
		boolean idle = false;
		
		if (!isJumping())
		{
			if (climbing)
			{
				if (!tryClimb(delta))
				{
					idle = true;
				}
			}
			else
			{
				idle = true;
			}
		}
		
		if (idle)
		{
			float speed = 3f * delta;
			
			setOnGround(!tryMove(0, -speed));
		}
	}
}