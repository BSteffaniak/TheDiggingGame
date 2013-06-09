package net.foxycorndog.thedigginggame.noise;

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
	private	int	momentum;
	
	public NoiseMap()
	{
		
	}
	
	public void generate()
	{
		
	}
	
	public void generate(NoiseMap left, NoiseMap right)
	{
		NoiseMap prev = null;
		
		if (left != null)
		{
			prev = left;
		}
		else if (right != null)
		{
			prev = right;
		}
		
		if (prev != null)
		{
			momentum = prev.momentum;
		}
		
		
	}
}