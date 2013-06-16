package net.foxycorndog.thedigginggame.map.terrain;

import java.util.Random;

/**
 * 
 * 
 * @author	Braden Steffaniak
 * @since	Jun 9, 2013 at 6:30:41 PM
 * @since	v0.4
 * @version	Jun 9, 2013 at 6:30:41 PM
 * @version	v0.4
 */
public class NoiseMap
{
	private	int		value;
	private	int		momentum;

	private	int		values[];
	
	private			final	int		WIDTH, HEIGHT;
	
	private	static	final	boolean	trim	= true;

	public	static	final	Random	random	= new Random();

	public NoiseMap(int width, int height)
	{
		this.WIDTH  = width;
		this.HEIGHT = height;

		value    = 10;
		
		long seed   = System.nanoTime();
		setSeed(seed);
		
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

	public void generate()
	{
		generate(null, null);
	}

	public void generate(NoiseMap left, NoiseMap right)
	{
		int      index = 0;

		int      lh    = 0;
		int      rh    = 0;

		if (left != null)
		{
			index    = 0;
			lh       = left.values[left.WIDTH - 1];
			value = lh;
		}
		else if (right != null)
		{
			index    = WIDTH + 1;
			value = right.values[0];
		}
		else
		{
			value = random.nextInt(HEIGHT);
		}
		
		if (right != null)
		{
			rh = right.values[0];
		}

		while (true)
		{
			if (left != null)
			{
				index++;
				
				if (index > values.length)
				{
					break;
				}
				
				if (right != null)
				{
					int   sign  = rh > value ? 1 : (rh == value ? 0 : -1);
	
					float slope = (float)(rh - value) / (WIDTH - index - 1);
	
					if (Math.abs(slope) <= 1)
					{
						value += random.nextInt(3) - 1;
					}
					else
					{
						value += sign;
					}
				}
			}
			else if (right != null)
			{
				index--;
				
				if (index < 1)
				{
					break;
				}
			}

			if (left == null || right == null)
			{
				if (left == null && right == null)
				{
					index++;

					if (index > values.length)
					{
						break;
					}
				}
				
				value += random.nextInt(3) - 1;
			}
			
			if (trim)
			{
				value = value < 0 ? 0 : value;
				value = value > HEIGHT - 1 ? HEIGHT - 1 : value;
			}
			
			values[index - 1] = value;
		}
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
