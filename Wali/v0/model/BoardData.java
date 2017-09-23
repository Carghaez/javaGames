package javaGames.Wali.v0.model;

import javaGames.Wali.v0.controller.*;
import javaGames.Wali.v0.*;
import javaGames.Wali.v0.view.*;
import java.util.Observable;

public class BoardData
	extends Observable
{
	private byte[][] matrice;  // 0 Player1 - 1 Player2 - 2 VUOTO
	private byte RigSel;
	private byte ColSel;

	public BoardData()
	{
		matrice=new byte[5][6];
		RigSel=ColSel=-1;
	}

	public byte getPedine(byte simb)
	{
		byte NumPedine=0;
		for(int i=0;i<matrice.length;i++)
			for(int j=0;j<matrice[i].length;j++)
				if(matrice[i][j]==simb) NumPedine++;
		return NumPedine;
	}

	public void resetCampi()
	{
		for(int i=0;i<matrice.length;i++)
			for(int j=0;j<matrice[i].length;j++)
				matrice[i][j]=(byte)2;
		Refresh();
	}

	public void Refresh()
	{
		setChanged();
		notifyObservers(matrice);
	}

	public void setCampo(byte simb,byte _r,byte _c)
	{
		matrice[_r][_c]=simb;
		setChanged();
		notifyObservers(matrice);
	}

	public byte getCampo(byte _r,byte _c)
	{
		return matrice[_r][_c];
	}

	public byte[][] getMatrix(){return matrice;}
	public void setSelected(byte r,byte c){ RigSel=r; ColSel=c; Refresh();}
	public byte getRigSel(){return RigSel;}
	public byte getColSel(){return ColSel;}
}