package net.foxycorndog.thedigginggame.actor;

import net.foxycorndog.jfoxylib.Frame;
import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.thedigginggame.item.Inventory;
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
public class Actor
{
	private boolean			moving, movingHorizontally, movingVertically;
	private boolean			onGround, jumping;
	private boolean			increaseRot;
	private boolean			sprinting;
	private boolean			focused;
	
	private int				width, height;
	private int				facing, oldFacing;
	
	private float			x, y;
	private float			mx, my;
	private float			jumpHeight, startY;
	private float			speed;
	private float			rotation;
	
	private	Inventory		inventory;
	
	private SpriteSheet		sprites;
	
	private Bundle			bundle;
	
	private Map				map;
	
	private float			color[];
	
	public static final int	LEFT = 0, FORWARD = 1, RIGHT = 2, BACKWARD = 3;
	
	/**
	 * Creates an actor with the specified characteristics.
	 * 
	 * @param map The map to add the actor to.
	 * @param width The width of the sprite of the Actor.
	 * @param height The height of the sprite of the Actor.
	 * @param speed The speed of the actor when walking.
	 * @param jumpHeight The height in which the Actor can jump.
	 */
	public Actor(Map map, int width, int height, float speed, float jumpHeight)
	{
		this.map         = map;
		
		this.width       = width;
		this.height      = height;
		
		this.speed       = speed;
		this.jumpHeight  = jumpHeight;
		
		this.facing      = FORWARD;
		
		this.increaseRot = true;
		
		this.color       = new float[] { 1, 1, 1, 1 };
		
		inventory        = new Inventory(9 * 3);
	}
	
	/**
	 * @return Whether this Actor is focused on.
	 */
	public boolean isFocused()
	{
		return focused;
	}
	
	/**
	 * Set whether this Actor is to be focused on. Centers the Actor on
	 * the screen any time it is moved.
	 * 
	 * @param focused Whether or not to focus the Actor.
	 */
	public void setFocused(boolean focused)
	{
		if (focused != this.focused)
		{
			this.focused = focused;
			
			if (focused)
			{
				map.setActorFocused(this);
			}
		}
	}
	
	/**
	 * Centers the Actor in the screen.
	 */
	public void center()
	{
		float scale = map.getGame().getScale();
		
		map.setLocation(-x + ((Frame.getWidth() / 2) / scale) - width / 2, -y + ((Frame.getHeight() / 2) / scale) - height / 2);
	}
	
	/**
	 * @return The absolute x position of the Actor on the Map.
	 */
	public float getX()
	{
		return x;
	}
	
	/**
	 * @return The absolute y position of the Actor on the Map.
	 */
	public float getY()
	{
		return y;
	}
	
	/**
	 * @return The x position of the Actor relative to the screen.
	 */
	public float getScreenX()
	{
		return x + map.getX();
	}
	
	/**
	 * @return The y position of the Actor relative to the screen.
	 */
	public float getScreenY()
	{
		return y + map.getY();
	}
	
	/**
	 * Set the absolute location of the Actor on the Map.
	 * 
	 * @param x The horizontal component to set.
	 * @param y The vertical component to set.
	 */
	public void setLocation(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Move the Actor the specified amount on the Map.
	 * 
	 * @param dx The displacement horizontal component.
	 * @param dy The displacement vertical component.
	 */
	public void move(float dx, float dy)
	{
		this.x += dx;
		this.y += dy;
		
		mx += dx;
		my += dy;
		
		if (focused)
		{
			center();
		}
	}
	
	/**
	 * Attempt to make a move in the direction that the specified values
	 * express.
	 * 
	 * @param dx The horizontal magnitude in which to try to move.
	 * @param dy The vertical magnitude in which to try to move.
	 * @return Whether or not a move was successful.
	 */
	public boolean tryMove(float dx, float dy)
	{
		float dx2 = dx;
		float dy2 = dy;
		
		move(dx2, dy2);
		
		boolean collision = map.isCollision(this);
		
		boolean moved     = !collision;
		
		while (collision && (Math.abs(dx2) >= 0.25f || Math.abs(dy2) > 0.25f))
		{
			move(-dx2, -dy2);
			
			dx2 /= 2;
			dy2 /= 2;
			
			move(dx2, dy2);
			
			collision = map.isCollision(this);
		}
		
		if (collision)
		{
			move(-dx2, -dy2);
		}
		
		return moved;
	}
	
	/**
	 * Move the Actor to the left at the rate of the Actor's speed.
	 * 
	 * @return Whether or not the Actor was able to move left.
	 */
	public boolean moveLeft(float delta)
	{
		facing = LEFT;
		
		return tryMove(-speed * delta, 0);
	}
	
	/**
	 * Move the Actor to the right at the rate of the Actor's speed.
	 * 
	 * @return Whether or not the Actor was able to move right.
	 */
	public boolean moveRight(float delta)
	{
		facing = RIGHT;
		
		return tryMove(speed * delta, 0);
	}
	
	/**
	 * Start jumping if the Actor is on the ground and not already
	 * jumping.
	 */
	public synchronized void jump()
	{
		if (!jumping && onGround)
		{
			jumping = true;
			
			startY = y;
		}
	}
	
	/**
	 * Get the Inventory instance of the Actor. It holds all of the
	 * Items that the Actor has acquired.
	 * 
	 * @return The Inventory instance of the Actor.
	 */
	public Inventory getInventory()
	{
		return inventory;
	}
	
	/**
	 * Set the Inventory instance that the Actor will use to hold all
	 * of the Items that the Actor will acquire.
	 * 
	 * @param inventory The Inventory instance that the Actor will use
	 * to hold all of the Items that the Actor will acquire.
	 */
	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
	
	/**
	 * @return The width of the sprite of the Actor.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @return The height of the sprite of the Actor.
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * @return Whether or not the Actor is on the ground.
	 */
	public boolean isOnGround()
	{
		return onGround;
	}
	
	/**
	 * @return The Bundle that is used to render the Actor.
	 */
	public Bundle getBundle()
	{
		return bundle;
	}
	
	/**
	 * Set the Bundle in which to use to render the Actor with.
	 * 
	 * @param bundle The Bundle to use.
	 */
	public void setBundle(Bundle bundle)
	{
		this.bundle = bundle;
	}
	
	/**
	 * @return The SpriteSheet that is used to render the Actor.
	 */
	public SpriteSheet getSprites()
	{
		return sprites;
	}
	
	/**
	 * Set the SpriteSheet in which to use to render the Actor with.
	 * 
	 * @param sprites The SpriteSheet to use.
	 */
	public void setSprites(SpriteSheet sprites)
	{
		this.sprites = sprites;
	}
	
	/**
	 * @return The direction that the Actor is currently facing.
	 */
	public int getFacing()
	{
		return facing;
	}
	
	/**
	 * @return The rotation of the Actor.
	 */
	public float getRotation()
	{
		return rotation;
	}
	
	/**
	 * @return Whether the Actor is currently moving in any direction.
	 */
	public boolean isMoving()
	{
		return moving;
	}
	
	/**
	 * @return Whether the Actor is currently moving horizontally.
	 */
	public boolean isMovingHorizontally()
	{
		return movingHorizontally;
	}

	/**
	 * @return Whether the Actor is currently moving vertically.
	 */
	public boolean isMovingVertically()
	{
		return movingVertically;
	}
	
	/**
	 * @return Whether the Actor is currently sprinting.
	 */
	public boolean isSprinting()
	{
		return sprinting;
	}
	
	/**
	 * Set whether the Actor is sprinting or not.
	 * 
	 * @param sprinting Whether the Actor is sprinting.
	 */
	public void setSprinting(boolean sprinting)
	{
		if (sprinting != this.sprinting)
		{
			if (sprinting)
			{
				speed *= 1.6f;
			}
			else
			{
				speed /= 1.6f;
			}
			
			this.sprinting = sprinting;
		}
	}
	
	/**
	 * @return The rgba float color array of this Actor.
	 */
	public float[] getColor()
	{
		return color;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}
	
	/**
	 * Render the Actor to the screen at its absolute x and y location.
	 */
	public void render()
	{
		GL.pushMatrix();
		{
			GL.translate(x, y, -1);
			GL.setColor(color[0], color[1], color[2], color[3]);
			
			bundle.render(GL.TRIANGLES, sprites);
		}
		GL.popMatrix();
	}
	
	/**
	 * Update the Actor every frame.
	 */
	public void update(float delta)
	{
		movingHorizontally = mx != 0;
		movingVertically   = my != 0;
		
		moving = movingHorizontally || movingVertically;
		
		mx     = 0;
		my     = 0;
		
		float spd = 5  * (sprinting ? 1.7f  : 1);
		float max = 55 * (sprinting ? 1.25f : 1);
		
		if (movingHorizontally && oldFacing == facing)
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
		
		oldFacing = facing;
		
		if (jumping)
		{
			float speed = 3f * delta;
			
			if (tryMove(0, speed))
			{
				onGround = false;
			}
			else
			{
				jumping = false;
			}
			
			if (y >= startY + jumpHeight)
			{
				jumping = false;
			}
		}
		
		if (!jumping)
		{
			float speed = 3f * delta;
			
			onGround    = !tryMove(0, -speed);
		}
		
		inventory.update();
	}
}