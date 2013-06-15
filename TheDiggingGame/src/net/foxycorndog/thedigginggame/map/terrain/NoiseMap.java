package net.foxycorndog.thedigginggame.map.terrain;

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
	private int		width, height;
	private	int		momentum;

	private	Random	random;

	private	int		values[];

	public NoiseMap(int width, int height)
	{
		this.width  = width;
		this.height = height;

		momentum    = 0;

		random      = new Random();
	}

	/**
	 * Set the seed to use to generate random numbers.
	 * 
	 * @param seed The seed to use to generate random numbers.
	 */
	public void setSeed(long seed)
	{
		random.setSeed(seed);
	}

	public void generate()
	{
		generate(null, null);
	}

	public void generate(NoiseMap left, NoiseMap right)
	{
		NoiseMap prev  = null;

		int      index = 0;

		int      lh    = 0;
		int      rh    = 0;

		if (left != null)
		{
			prev  = left;

			index = 0;
			lh    = left.values[left.width - 1];
		}
		else if (right != null)
		{
			prev  = right;

			index = width - 1;
			rh    = right.values[0];
		}

		if (prev != null)
		{
			momentum = prev.momentum;
		}
		else
		{
			momentum = random.nextInt(height);
		}

		boolean finished = false;

		while (!finished)
		{
			values[index] = momentum;

			if (left != null)
			{
				index++;

				if (right != null)
				{
					int   sign  = rh > momentum ? 1 : (rh == momentum ? 0 : -1);

					float slope = (ri - momentum - sign) / (width - index);

					if (slope <= 1)
					{
						momentum += random.nextInt(3) - 1;
					}
					else
					{
						momentum += sign;
					}
				}
			}
			else if (right != null)
			{
				index--;
			}

			if (left == null || right == null)
			{
				momentum += random.nextInt(3) - 1;
			}
		}
	}
}
