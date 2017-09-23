package javaGames.Halma.v0.model;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.view.*;
import javaGames.Halma.v0.*;

public class GameData
{

	public byte stato; // 0 - Seleziona pedina,  1 - Effettua movimento o salto, 2 - Effettua un ulteriore salto, 3 - Conferma lo step
	public byte turno; // 0 - Player0, 1 - Player1
	public Giocatore[] Player;
	public int[][] scacchiera;

	public GameData()
	{
		Player = new Giocatore[2];
		Player[0] = new Giocatore();
		Player[1] = new Giocatore();
		Player[0].simbolo = "0";
		Player[1].simbolo = "1";
		scacchiera = new int[10][10];
	}

	public void initPiece()
	{
		for(byte i = 0; i < 10; i++)
			for(byte j = 0; j < 10; j++)
			{
				scacchiera[i][j] = chkPos(i,j);
				if(scacchiera[i][j] != 2)
				{
					Player[scacchiera[i][j]].pedine.add(new posData(i,j));
				}
			}
	}

	public byte chkPos(posData p)
	{
		return chkPos(p.x, p.y);
	}

	public byte chkPos(int i, int j) {
		//Base Player1
		if((i==0 || i==1) && j>=6){
			return 1;
		}
		if(i==2 && j>=7){
			return 1;
		}
		if(i==3 && j>=8){
			return 1;
		}

		//Base Player0
		if(i==6 && j<=1){
			return 0;
		}
		if(i==7 && j<=2){
			return 0;
		}
		if((i==8 || i==9) && j<=3){
			return 0;
		}
		//Casella vuota
		return 2;
	}
}