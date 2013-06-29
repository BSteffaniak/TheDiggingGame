package net.foxycorndog.thedigginggame.map.terrain;

import java.util.Random;

/**
 * Class used to generate the values needed to determine the height
 * of a Chunk's generated terrain.
 * 
 * @author	Braden Steffaniak
 * @since	Jun 9, 2013 at 6:30:41 PM
 * @since	v0.4
 * @version	Jun 9, 2013 at 6:30:41 PM
 * @version	v0.4
 */
public class NoiseMap
{
	private	int			value;
	private	int			momentum;
	
	private	NoiseMap	left, right;
	
	private	int			values[];
	
	private			final	int		width, height;
	
	private	static	final	boolean	trim	= true;

	public	static	final	Random	random	= new Random();
	
	/**
	 * Create a NoiseMap instance to create values between the specified
	 * width and height values given.
	 * 
	 * @param width The width of Map to generate values for (How many
	 * 		values will be generated in the end)
	 * @param height The maximum height that the values can reach.
	 * adsf
	 * as
	 * dasf
	 * fdasdfasfda
	 */
	public NoiseMap(int width, int height, NoiseMap left, NoiseMap right)
	{
		this.width  = width;
		this.height = height;
		
		this.left   = left;
		this.right  = right;
		
		long seed   = System.nanoTime();
		setSeed(1);
		
		values      = new int[width];
	}

	/**
	 * Set the seed to use to generate random numbers.
	 * 
	 * @param seed The seed to use to generate random numbers.
	 */
	public static final void setSeed(long seed)
	{
		random.setSeed(seed);
	}
	
	public NoiseMap getLeft()
	{
		return left;
	}
	
	public void setLeft(NoiseMap map)
	{
		this.left = map;
	}
	
	public NoiseMap getRight()
	{
		return right;
	}
	
	public void setRight(NoiseMap map)
	{
		this.right = map;
	}
	
	/**
	 * Generate the values that determine the height of a Chunk's
	 * generated terrain. The values are based upon the left and
	 * right NoiseMaps given.
	 * 
	 * @param left The NoiseMap that is directly left of the current
	 * 		NoiseMap.
	 * @param right The NoiseMap that is directly right of the current
	 * 		NoiseMap.
	 * @return An array of two NoiseMaps that are to the left and right
	 * 		of the newly generated values.
	 */
	public void generate()
	{
		if (left == null)
		{
			left = new NoiseMap(width, height, null, this);
		}
		if (right == null)
		{
			right = new NoiseMap(width, height, this, null);
		}
		
//		applyNoise(30, 3);
//		applyNoise(10, 5);
//		applyNoise(10, 7);
		applyNoise(4, 13);
		applyNoise(2, 8);
//		applyNoise(2, 9);
		applyNoise(22, 5);
//		raiseValues(4, 15, 3);
//		raiseValues(9, 15, 3);
		
//		for (int i = 0; i < values.length - 2; i++)
//		{
//			int dif = (values[i] - values[i + 1]) + (values[i + 2] - values[i + 1]);
////			System.out.println(dif);
////			if (Math.abs(dif) > 2)
////			{
////				values[i + 1] = values[i];
////				
//////				raiseValues(i + 1, 5, 2);
////				
//////				values[i + 1]--;
////			}
//		}
		
//		int      index = 0;
//
//		int      lh    = 0;
//		int      rh    = 0;
//
//		if (left != null)
//		{
//			index    = 0;
//			lh       = left.values[left.WIDTH - 1];
//			value = lh;
//		}
//		else if (right != null)
//		{
//			index    = WIDTH + 1;
//			value = right.values[0];
//		}
//		else
//		{
//			value = random.nextInt(HEIGHT);
//		}
//		
//		if (right != null)
//		{
//			rh = right.values[0];
//		}
//
//		while (true)
//		{
//			if (left != null)
//			{
//				index++;
//				
//				if (index > values.length)
//				{
//					break;
//				}
//				
//				if (right != null)
//				{
//					int   sign  = rh > value ? 1 : (rh == value ? 0 : -1);
//	
//					float slope = (float)(rh - value) / (WIDTH - index - 1);
//	
//					if (Math.abs(slope) <= 1)
//					{
//						value += random.nextInt(3) - 1;
//					}
//					else
//					{
//						value += sign;
//					}
//				}
//			}
//			else if (right != null)
//			{
//				index--;
//				
//				if (index < 1)
//				{
//					break;
//				}
//			}
//
//			if (left == null || right == null)
//			{
//				if (left == null && right == null)
//				{
//					index++;
//
//					if (index > values.length)
//					{
//						break;
//					}
//				}
//				
//				value += random.nextInt(3) - 1;
//			}
//			
//			if (trim)
//			{
//				value = value < 0 ? 0 : value;
//				value = value > HEIGHT - 1 ? HEIGHT - 1 : value;
//			}
//			
//			values[index - 1] = value;
//		}
	}
	
	private void applyNoise(int frequency, int size)
	{
		for (int i = -size / 2; i < values.length + size / 2; i++)
		{
			if (width - frequency <= 0 || random.nextInt(width - frequency) == 0)
			{
				raiseValues(i, size, Math.max(1, random.nextInt(size / 4 * 3)) + 1);
			}
		}
	}
	
	private void raiseValues(int index, int size, int height)
	{
		height++;
		
		int maxI = index;
		int maxV = 0;
		
		int occurrences = 0;
		
		for (int x = -size - 1; x < size * 2 + 1; x++)
		{
			float x2 = x - size / 2f + index;
			int   x3 = Math.round(x2);
			
			int   yv = getValue(x3);
			
			int   y2 = yv;
			
			while (distance(index, yv - (size - height), x3, y2 + 1) <= size)
			{
				y2++;
			}
			
			if (y2 > maxV)
			{
				maxV = y2;
				maxI = x3;
				
				occurrences = 1;
			}
			else if (y2 == maxV)
			{
				occurrences++;
			}
			
			setValue(x3, y2);
		}
		
		if (occurrences == 1)
		{
			setValue(maxI, maxV - 1);
		}
	}
	
	private int getValue(int index)
	{
		if (index < 0 || index >= width)
		{
			if (index < 0)
			{
				index += left.width;
				
				return left.getValue(index);
			}
			else
			{
				index -= width;
				
				return right.getValue(index);
			}
		}
		
		return values[index];
	}
	
	private void setValue(int index, int value)
	{
		if (index < 0 || index >= width)
		{
			if (index < 0)
			{
				index += left.width;
				
				left.setValue(index, value);
			}
			else
			{
				index -= width;
				
				right.setValue(index, value);
			}
			
			return;
		}
		
		values[index] = value;
	}
	
	/**
	 * Get the distance from two points.
	 * 
	 * @param x1 The horizontal position of the first point.
	 * @param y1 The vertical position of the first point.
	 * @param x2 The horizontal position of the second point.
	 * @param y2 The vertical position of the second point.
	 * @return The distance between the two points.
	 */
	private double distance(float x1, float y1, float x2, float y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	/**
	 * Get the Integer array that holds all of the values.
	 * 
	 * @return The Integer array that holds all of the values.
	 */
	public int[] getValues()
	{
		return values;
	}
}
