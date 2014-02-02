package net.foxycorndog.thedigginggame.map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.foxycorndog.jfoxylib.Color;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Sep 25, 2013 at 8:48:25 PM
 * @since	v0.4
 * @version	Sep 25, 2013 at 8:48:25 PM
 * @version	v0.4
 */
public class DayCycle
{
	private int		height;
	
	private double	time;
	
	private int		data[];
	
	/**
	 * 
	 * 
	 * @param imageLocation
	 * @throws IOException
	 */
	public DayCycle(String imageLocation) throws IOException
	{
		this(ImageIO.read(new File(imageLocation)));
	}
	
	/**
	 * 
	 * 
	 * @param image
	 */
	public DayCycle(BufferedImage image)
	{
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.BITMASK);
		
		Graphics2D g = img.createGraphics();
		g.clearRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		data   = ((DataBufferInt)img.getData().getDataBuffer()).getData();
		
		height = image.getHeight();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Color getCurrentColor()
	{
		int offset = getCurrentImageOffset();
		
		int value = data[offset];
		
		int r = (value >> 16) & 0xFF;
		int g = (value >>  8) & 0xFF;
		int b = (value >>  0) & 0xFF;
		
		Color color = new Color(r, g, b);
		
		return color;
	}
	
	/**
	 * 
	 * 
	 * @param delta
	 */
	public void update(float delta)
	{
		time += (delta * height) / 10000;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private int getCurrentImageOffset()
	{
		double num = time % height;
		
		return (int)num;
	}
}