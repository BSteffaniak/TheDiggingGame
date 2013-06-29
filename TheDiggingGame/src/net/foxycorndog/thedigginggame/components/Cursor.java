package net.foxycorndog.thedigginggame.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import net.foxycorndog.jfoxylib.opengl.GL;
import net.foxycorndog.jfoxylib.opengl.bundle.Bundle;
import net.foxycorndog.jfoxylib.opengl.texture.Texture;

/**
 * Class that is used to organize information for the cursor.
 * 
 * @author	Braden Steffaniak
 * @since	Feb 24, 2013 at 12:27:58 AM
 * @since	v0.1
 * @version Feb 25, 2013 at 6:20:01 AM
 * @version	v0.1
 */
public class Cursor
{
	private int		size;
	private int		x, y;
	
	private Texture	texture;
	
	private Bundle	bundle;
	
	/**
	 * Create a cursor with the specified size. The size indicates the
	 * length of all four sides of a square.
	 * 
	 * @param size The size of the sides.
	 */
	public Cursor(int size)
	{
		this.size = size;
		
		BufferedImage img = new BufferedImage(size, size, BufferedImage.BITMASK);
		
		Graphics2D g = img.createGraphics();
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, size - 1, size - 1);
		g.dispose();
		
		texture = new Texture(img);
		
		bundle = new Bundle(3 * 2, 2, true, false);
		
		bundle.beginEditingVertices();
		{
			bundle.addVertices(GL.genRectVerts(0, 0, size, size));
		}
		bundle.endEditingVertices();
		
		bundle.beginEditingTextures();
		{
			bundle.addTextures(GL.genRectTextures(texture.getImageOffsets()));
		}
		bundle.endEditingTextures();
	}
	
	/**
	 * Renders the cursor at the specified location.
	 * 
	 * @param x The horizontal location to render to.
	 * @param y The vertical location to render to.
	 * @param z The oblique location to render to.
	 */
	public void render(float x, float y, float z)
	{
		GL.pushMatrix();
		{
			GL.translate(x, y, z);
			
			bundle.render(GL.TRIANGLES, texture);
		}
		GL.popMatrix();
	}
	
	/**
	 * @return The absolute horizontal position of the Cursor relative to
	 * 		Tiles.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * @return The absolute vertical position of the Cursor relative to
	 * 		Tiles.
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Set the location of the Cursor relative to the blocks.
	 * 
	 * @param x The horizontal location of the Cursor.
	 * @param y The vertical location of the Cursor.
	 */
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}