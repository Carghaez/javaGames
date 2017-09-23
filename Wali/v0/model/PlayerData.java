package javaGames.Wali.v0.model;

import javaGames.Wali.v0.controller.*;
import javaGames.Wali.v0.*;
import javaGames.Wali.v0.view.*;
import java.util.Observable;

public class PlayerData
	extends Observable
{
	private String nick;
	private String symbol;
	private byte vittorie;
	private boolean isCPU;
	private byte pedine;

	public PlayerData(String n,String s,boolean c)
	{
		nick=n;
		symbol=s;
		isCPU=c;
		vittorie=0;
	}

	public void setPedine(byte num)
	{
		pedine=num;
		setChanged();
		notifyObservers(new Byte(pedine));
	}

	public String getNick(){ return nick; }
	public String getSymbol(){ return symbol; }
	public byte getVittorie(){ return vittorie; }
	public byte getPedine(){return pedine;}
	public boolean isCPU(){ return isCPU; }
	public void addVittoria()  {
		vittorie++;
		setChanged();
		notifyObservers(new Byte(pedine));
	}
}
