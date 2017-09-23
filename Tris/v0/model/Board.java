package javaGames.Tris.v0.model;

import javaGames.Tris.v0.controller.*;
import javaGames.Tris.v0.*;
import javaGames.Tris.v0.view.*;

public class Board
{
	final int EMPTY = -1;
	int[][] coord;

	public Board(int righe, int colonne)
	{
		coord = new int[righe][colonne];
		resetCoord();
	}

	public void resetCoord()
	{
		for(int r = 0; r < coord.length; r++)
			for(int c = 0; c < coord[r].length; c++)
				coord[r][c] = EMPTY;
	}

	public boolean isEmpty(int r, int c)
	{
		return isPieceOfPlayer(EMPTY, r, c);
	}

	public boolean isPieceOfPlayer(int ID, int r, int c)
	{
		return (coord[r][c] == ID) ? true : false;
	}

	public void setPiece(int ID, int r, int c)
	{
		coord[r][c] = ID;
	}
}