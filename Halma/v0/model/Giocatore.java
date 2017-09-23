package javaGames.Halma.v0.model;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.view.*;
import javaGames.Halma.v0.*;

import java.util.*;
import javax.swing.JOptionPane;

public class Giocatore
{
	private String nick;
	public ArrayList<posData> pedine;
	public boolean isCpu;
	public String simbolo;

	public Giocatore()
	{
		pedine = new ArrayList<posData>();
	}

	public boolean isOfPlayer(posData p)
	{
		for(int i = 0 ; i < pedine.size() ; i++)
			if(pedine.get(i).equals(p))
				return true;
		return false;
	}

	public void deletePiece(posData p)
	{
		for(int i = 0 ; i < pedine.size() ; i++)
			if(pedine.get(i).equals(p))
				pedine.remove(i);
	}

	public void setName(String s)
	{
		nick = s;
	}

	public String getName()
	{
		return nick;
	}
}
