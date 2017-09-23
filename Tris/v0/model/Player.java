package javaGames.Tris.v0.model;
import javaGames.Tris.v0.controller.*;
import javaGames.Tris.v0.*;
import javaGames.Tris.v0.view.*;

public class Player
{
	private String nick;
	private char symbol;
	private int score;

	public Player(String n, char s)
	{
		nick = n;
		symbol = s;
		score = 0;
	}

	public int getScore()
	{
		return score;
	}

	public void setSymbol(char s)
	{
		symbol = s;
	}

	public void addParity()
	{
		score += 1;
	}

	public void addWin()
	{
		score += 3;
	}
}