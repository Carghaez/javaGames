package javaGames.Wali.v1.model;

import javaGames.Wali.v1.controller.*;
import javaGames.Wali.v1.*;
import javaGames.Wali.v1.view.*;
import java.util.*;
import javax.swing.*;

public class PlayerData
	extends Observable
{
	private int ID;
	private String nick;
	private String symbol;
	private ImageIcon icon;
	private byte vittorie;
	private boolean isCPU;
	private ArrayList<PieceData> pedine;
	public int nextCPUrow;
	public int nextCPUcol;

	public PlayerData(int id, String n, String s, boolean c)
	{
		ID = id;
		nick=n;
		symbol=s;
		isCPU=c;
		vittorie=0;
		pedine = new ArrayList<PieceData>();
		nextCPUrow = nextCPUcol = -1;
	}

	public PlayerData(int id, String n, ImageIcon i, String col, boolean c)
	{
		ID = id;
		nick=n;
		icon=i;
		isCPU=c;
		symbol=col;
		vittorie=0;
		pedine = new ArrayList<PieceData>();
	}

	public String getNick(){ return nick; }
	public String getSymbol(){ return symbol; }
	public ImageIcon getIcon(){ return icon; }
	public int getID(){ return ID; }

	public byte getVittorie()
	{
		return vittorie;
	}

	public void resetVittorie(){ vittorie=0; }

	public boolean isCPU(){ return isCPU; }

	public void refresh()
	{
		setChanged();
		notifyObservers(new Byte(vittorie));
	}

	public void addVittoria()
	{
		vittorie++;
		refresh();
	}

	public void removePiece(byte r, byte c)
	{
		pedine.remove(getPieceByCoord(r,c));
		refresh();
	}

	public PieceData getPieceByCoord(byte r, byte c)
	{
		for(int i=0; i < pedine.size(); i++)
		{
			if(pedine.get(i).riga == r && pedine.get(i).colonna == c)
				return pedine.get(i);
		}
		JOptionPane.showMessageDialog(null, "c'è qualcosa che non va...");
		return null;
	}

	public void addPiece(byte r, byte c)
	{
		pedine.add(new PieceData(r, c));
		setChanged();
		notifyObservers(new Byte(vittorie));
	}

	public void clearAllData()
	{
		pedine.clear();
		resetVittorie();
		setChanged();
		notifyObservers(new Byte(vittorie));
	}

	public void removeAllPieces()
	{
		pedine.clear();
		setChanged();
		notifyObservers(new Byte(vittorie));
	}

	public int getCurrentPieces()
	{
		return pedine.size();
	}

	public PieceData getPieceByIndex(int i)
	{
		return pedine.get(i);
	}
}
