package net.foxycorndog.thedigginggame.actor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.SpriteSheet;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;
import net.foxycorndog.thedigginggame.TheDiggingGame;
import net.foxycorndog.thedigginggame.map.Map;

/**
 * Class that is used as a Player for the game. Has special sprites and
 * other characteristics.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 22, 2013 at 4:23:36 AM
 * @since	v0.1
 * @version Feb 22, 2013 at 4:23:37 AM
 * @version	v0.1
 */
public class Player extends Actor
{
	private QuickBar	quickBar;
	
	/**
	 * Creates a Player. Sets up the vertices and textures for the bundle
	 * that is used in the Actor.
	 * 
	 * @param map The Map to add the Player to.
	 */
	public Player(Map map)
	{
		super(map, 16, 32, 1.25f, 25);
		
		quickBar = new QuickBar();
		
		SpriteSheet sprites = null;
		
		try
		{
			sprites = new SpriteSheet(TheDiggingGame.getResourcesLocation() + "res/images/actors/player/skins/default.png", 16, 8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		Bundle bundle = new Bundle(3 * 2 * 6 * 4, 2, true, false);
		
		bundle.beginEditingVertices();
		{
			// LEFT
			// Head
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 8));
			// Right arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Torso
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Left arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Right leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Left leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));

			// FORWARD
			// Head
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 8));
			// Right arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Torso
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 12));
			// Left arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Right leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Left leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));

			// RIGHT
			// Head
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 8));
			// Right arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Torso
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Left arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Right leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Left leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));

			// BACKWARD
			// Head
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 8));
			// Right arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Torso
			bundle.addVertices(GL.genRectVerts(0, 0, 8, 12));
			// Left arm
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Right leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
			// Left leg
			bundle.addVertices(GL.genRectVerts(0, 0, 4, 12));
		}
		bundle.endEditingVertices();
		
		bundle.beginEditingTextures();
		{
			// LEFT
			// Head
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(4, 2, 2, 2)));
			// Right arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3)));
			// Torso
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(5, 5, 2, 3)));
			// Left arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3), true, false));
			// Right leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3)));
			// Left leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3), true, false));

			// FORWARD
			// Head
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(2, 2, 2, 2)));
			// Right arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3)));
			// Torso
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(5, 5, 2, 3)));
			// Left arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3), true, false));
			// Right leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3)));
			// Left leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3), true, false));

			// RIGHT
			// Head
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(0, 2, 2, 2)));
			// Right arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3)));
			// Torso
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(5, 5, 2, 3)));
			// Left arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3), true, false));
			// Right leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3)));
			// Left leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3), true, false));

			// BACKWARD
			// Head
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(2, 2, 2, 2)));
			// Right arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3)));
			// Torso
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(5, 5, 2, 3)));
			// Left arm
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(13, 5, 1, 3), true, false));
			// Right leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3)));
			// Left leg
			bundle.addTextures(GL.genRectTextures(sprites.getImageOffsets(1, 5, 1, 3), true, false));
		}
		bundle.endEditingTextures();
		
		setSprites(sprites);
		setBundle(bundle);
	}
	
	public void render()
	{
		Bundle bundle       = getBundle();
		SpriteSheet sprites = getSprites();
		int facing          = getFacing();
		float rotation      = getRotation();
		float color[]       = getColor();
		
		GL.pushMatrix();
		{
			GL.translate(getX(), getY(), -1);
			GL.setColor(color[0], color[1], color[2], color[3]);
			
			renderAppendages(bundle, sprites, facing, rotation);
		}
		GL.popMatrix();
	}
	
	private void renderAppendages(Bundle bundle, SpriteSheet sprites, int facing, float rotation)
	{
		int offset = facing * 3 * 2 * 6;
		
		GL.translate(0, Math.abs(rotation) / 23f, 0);
		
		renderHead    (3 * 2 * 0 + offset, bundle, sprites,  0,            facing);
		renderRightArm(3 * 2 * 1 + offset, bundle, sprites,  rotation / 2, facing);
		renderTorso   (3 * 2 * 2 + offset, bundle, sprites,  0,            facing);
		renderLeftArm (3 * 2 * 3 + offset, bundle, sprites, -rotation / 2, facing);
		renderRightLeg(3 * 2 * 4 + offset, bundle, sprites,  rotation,     facing);
		renderLeftLeg (3 * 2 * 5 + offset, bundle, sprites, -rotation,     facing);
	}

	/**
	 * Renders the Player's head to the screen with the specified
	 * rotation and facing.
	 * 
	 * @param offset The offset in which the head is located in the
	 * 		Buffer.
	 * @param bundle The Bundle in which to use to render the head.
	 * @param sprites The SpriteSheet to use to render the textures to the
	 * 		head.
	 * @param rotation The rotation to rotate the head to.
	 * @param facing The direction that the head will face.
	 */
	private void renderHead(int offset, Bundle bundle, SpriteSheet sprites, float rotation, int facing)
	{
		GL.pushMatrix();
		{
			if (facing == Actor.FORWARD || facing == Actor.BACKWARD)
			{
				GL.translate(4, 12 + 12, 0);
			}
			else if (facing == Actor.LEFT || facing == Actor.RIGHT)
			{
				GL.translate(4, 12 + 12, 0);
				
				if (rotation != 0)
				{
					GL.translate(2, 10, 0);
					
					GL.rotate(0, 0, rotation);
					
					GL.translate(-2, -10, 0);
				}
			}
			
			bundle.render(GL.TRIANGLES, offset, 3 * 2, sprites);
		}
		GL.popMatrix();
	}

	/**
	 * Renders the Player's right arm to the screen with the specified
	 * rotation and facing.
	 * 
	 * @param offset The offset in which the right arm is located in the
	 * 		Buffer.
	 * @param bundle The Bundle in which to use to render the right arm.
	 * @param sprites The SpriteSheet to use to render the textures to the
	 * 		right arm.
	 * @param rotation The rotation to rotate the right arm to.
	 * @param facing The direction that the right arm will face.
	 */
	private void renderRightArm(int offset, Bundle bundle, SpriteSheet sprites, float rotation, int facing)
	{
		GL.pushMatrix();
		{
			if (facing == Actor.FORWARD || facing == Actor.BACKWARD)
			{
				GL.translate(0, 12, 0);
			}
			else if (facing == Actor.LEFT || facing == Actor.RIGHT)
			{
				GL.translate(6, 12, (facing == LEFT ? -0.1f : 0.1f));

				if (rotation != 0)
				{
					GL.translate(2, 10, 0);
					
					GL.rotate(0, 0, rotation);
					
					GL.translate(-2, -10, 0);
				}
			}
			
			bundle.render(GL.TRIANGLES, offset, 3 * 2, sprites);
		}
		GL.popMatrix();
	}
	
	/**
	 * Renders the Player's torso to the screen with the specified
	 * rotation and facing.
	 * 
	 * @param offset The offset in which the torso is located in the
	 * 		Buffer.
	 * @param bundle The Bundle in which to use to render the torso.
	 * @param sprites The SpriteSheet to use to render the textures to the
	 * 		torso.
	 * @param rotation The rotation to rotate the torso to.
	 * @param facing The direction that the torso will face.
	 */
	private void renderTorso(int offset, Bundle bundle, SpriteSheet sprites, float rotation, int facing)
	{
		GL.pushMatrix();
		{
			if (facing == Actor.FORWARD || facing == Actor.BACKWARD)
			{
				GL.translate(4, 12, 0);
			}
			else if (facing == Actor.LEFT || facing == Actor.RIGHT)
			{
				GL.translate(6, 12, 0);

				if (rotation != 0)
				{
					GL.translate(2, 10, 0);
					
					GL.rotate(0, 0, rotation);
					
					GL.translate(-2, -10, 0);
				}
			}
			
			bundle.render(GL.TRIANGLES, offset, 3 * 2, sprites);
		}
		GL.popMatrix();
	}

	/**
	 * Renders the Player's left arm to the screen with the specified
	 * rotation and facing.
	 * 
	 * @param offset The offset in which the left arm is located in the
	 * 		Buffer.
	 * @param bundle The Bundle in which to use to render the left arm.
	 * @param sprites The SpriteSheet to use to render the textures to the
	 * 		left arm.
	 * @param rotation The rotation to rotate the left arm to.
	 * @param facing The direction that the left arm will face.
	 */
	private void renderLeftArm(int offset, Bundle bundle, SpriteSheet sprites, float rotation, int facing)
	{
		GL.pushMatrix();
		{
			if (facing == Actor.FORWARD || facing == Actor.BACKWARD)
			{
				GL.translate(4 + 8, 12, 0);
			}
			else if (facing == Actor.LEFT || facing == Actor.RIGHT)
			{
				GL.translate(6, 12, (facing == LEFT ? 0.1f : -0.1f));

				if (rotation != 0)
				{
					GL.translate(2, 10, 0);
					
					GL.rotate(0, 0, rotation);
					
					GL.translate(-2, -10, 0);
				}
			}
				
			bundle.render(GL.TRIANGLES, offset, 3 * 2, sprites);
		}
		GL.popMatrix();
	}
	
	/**
	 * Renders the Player's right leg to the screen with the specified
	 * rotation and facing.
	 * 
	 * @param offset The offset in which the right leg is located in the
	 * 		Buffer.
	 * @param bundle The Bundle in which to use to render the right leg.
	 * @param sprites The SpriteSheet to use to render the textures to the
	 * 		right leg.
	 * @param rotation The rotation to rotate the right leg to.
	 * @param facing The direction that the right leg will face.
	 */
	private void renderRightLeg(int offset, Bundle bundle, SpriteSheet sprites, float rotation, int facing)
	{
		GL.pushMatrix();
		{
			if (facing == Actor.FORWARD || facing == Actor.BACKWARD)
			{
				GL.translate(4, 0, 0);
			}
			else if (facing == Actor.LEFT || facing == Actor.RIGHT)
			{
				GL.translate(6, 0, (facing == LEFT ? -0.1f : 0.1f));

				if (rotation != 0)
				{
					GL.translate(2, 10, 0);
					
					GL.rotate(0, 0, rotation);
					
					GL.translate(-2, -10, 0);
				}
			}
			
			bundle.render(GL.TRIANGLES, offset, 3 * 2, sprites);
		}
		GL.popMatrix();
	}
	
	/**
	 * Renders the Player's left leg to the screen with the specified
	 * rotation and facing.
	 * 
	 * @param offset The offset in which the left leg is located in the
	 * 		Buffer.
	 * @param bundle The Bundle in which to use to render the left leg.
	 * @param sprites The SpriteSheet to use to render the textures to the
	 * 		left leg.
	 * @param rotation The rotation to rotate the left leg to.
	 * @param facing The direction that the left leg will face.
	 */
	private void renderLeftLeg(int offset, Bundle bundle, SpriteSheet sprites, float rotation, int facing)
	{
		GL.pushMatrix();
		{
			if (facing == Actor.FORWARD || facing == Actor.BACKWARD)
			{
				GL.translate(4 + 4, 0, 0);
			}
			else if (facing == Actor.LEFT || facing == Actor.RIGHT)
			{
				GL.translate(6, 0, (facing == LEFT ? 0.1f : -0.1f));

				if (rotation != 0)
				{
					GL.translate(2, 10, 0);
					
					GL.rotate(0, 0, rotation);
					
					GL.translate(-2, -10, 0);
				}
			}
			
			bundle.render(GL.TRIANGLES, offset, 3 * 2, sprites);
		}
		GL.popMatrix();
	}
	
	/**
	 * Get the QuickBar that the user has.
	 * 
	 * @return The QuickBar that the user has.
	 */
	public QuickBar getQuickBar()
	{
		return quickBar;
	}
	
	/**
	 * Class that is used to render the items that are the hot-keys.
	 * 
	 * @author	Braden Steffaniak
	 * @since	Mar 18, 2013 at 6:18:00 AM
	 * @since	v0.1
	 * @version Mar 18, 2013 at 6:18:00 AM
	 * @version	v0.1
	 */
	public class QuickBar
	{
		private Bundle	bundle;
		
		private Texture	slots[];
		
		/**
		 * Create the bar that shows what items are on the hot-keys.
		 */
		public QuickBar()
		{
			bundle = new Bundle(4 * 9 * 2, 2, true, false);
			
			BufferedImage slots[] = new BufferedImage[9];
			this.slots            = new Texture[9];
			
			for (int i = 1; i <= slots.length; i++)
			{
				try
				{
					slots[i - 1] = ImageIO.read(new File(TheDiggingGame.getResourcesLocation() + "res/images/quickbar/slot" + i + ".png"));
					
					this.slots[i - 1] = new Texture(slots[i - 1]);
				}
				catch (IOException e)
				{
//					e.printStackTrace();
				}
			}
			
			Texture slot = null;
			
			bundle.beginEditingVertices();
			{
				for (int i = 0; i < 9; i++)
				{
					if (this.slots[i] != null)
					{
						slot = this.slots[i];
					}

					bundle.addVertices(GL.genRectVerts(0, 0, slot.getWidth(), slot.getHeight()));
				}
			}
			bundle.endEditingVertices();
			
			slot = null;
			
			bundle.beginEditingTextures();
			{
			for (int i = 0; i < 9; i++)
				{
					if (this.slots[i] != null)
					{
						slot = this.slots[i];
					}
					
					bundle.addTextures(GL.genRectTextures(slot.getImageOffsets()));
				}
			}
			bundle.endEditingTextures();
		}
		
		/**
		 * Render the QuickBar to the bottom center of the screen.
		 */
		public void render()
		{
			Texture slot = null;
			
			for (int i = 0; i < 9; i++)
			{
				if (slots[i] != null)
				{
					slot = slots[i];
				}
				
				bundle.render(GL.TRIANGLES, slot);
				
				GL.translate(slot.getWidth(), 0, 0);
			}
		}
	}
}
