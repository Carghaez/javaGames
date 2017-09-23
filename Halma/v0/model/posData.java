package javaGames.Halma.v0.model;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.view.*;
import javaGames.Halma.v0.*;

public class posData
{
	public int x;
	public int y;

	public posData(int _y, int _x)
	{
		x=_x;
		y=_y;
	}

	@Override
	public String toString()
	{
		return "X: " + x +", Y: " + y;
	}

	public boolean equals(posData p)
	{
		if(p.x == this.x && p.y == this.y)
			return true;
		return false;
	}

	public int priority()
	{
		return y-x;
	}
}
