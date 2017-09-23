package javaGames.Halma.v0.model;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.view.*;
import javaGames.Halma.v0.*;

public class TreeNodo
{
	public TreeNodo posRoot;
	public posData p;
	public byte fase;

	public TreeNodo(posData _p, TreeNodo pR )
	{
		posRoot = pR;
		p = _p;
		fase=2;
	}

	public TreeNodo(posData _p, TreeNodo pR, byte _f )
	{
		posRoot = pR;
		p = _p;
		fase=_f;
	}
}
