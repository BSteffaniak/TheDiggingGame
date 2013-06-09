package net.foxycorndog.thedigginggame.noise;

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