package javaGames.Tris.v0.model;
import javaGames.Tris.v0.controller.*;
import javaGames.Tris.v0.*;
import javaGames.Tris.v0.view.*;

public class GameData
{
	public Board scacchiera;
	public Player[] giocatore;
	public int turno;

	public GameData(int numPlayer, int rig, int col)
	{
		turno = (int) (Math.random()*numPlayer);
		scacchiera = new Board(rig, col);
		giocatore = new Player[numPlayer];
		for(int i = 0; i < numPlayer; i++)
			giocatore[i] = new Player("Player"+i, (char)('a'+i));
	}

	public int nextTurn()
	{
		return turno = (turno + 1) % giocatore.length;
	}
}