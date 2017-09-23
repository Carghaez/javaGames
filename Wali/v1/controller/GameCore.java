package javaGames.Wali.v1.controller;

import javaGames.Wali.v1.model.*;
import javaGames.Wali.v1.model.GameData.GameRound;
import javaGames.Wali.v1.model.GameData.GameState;
import javaGames.Wali.v1.*;
import javaGames.Wali.v1.AI.*;

public class GameCore
{
	private GameData refModel;
	private AI CPU;

	public GameCore(GameData d)
	{
		refModel = d;
	}

	public int press(byte _i,byte _j)
	{
		GameRound turno = refModel.turno;
		BoardData scacchiera = refModel.getScacchiera();
		switch(refModel.stato)	//Controllo delle operazioni in base allo stato della partita
		{
			case Pos:
				if(scacchiera.getCampo(_i,_j) == 2)	//Controllo se il campo è vuoto
				{
					if(!isContigui(_i,_j))	//Controllo se esistono più di due pedine contigue
					{
						//Setto e aggiorno la pedina
						refModel.setCampo(1, (byte)turno.ordinal(), _i, _j);

						if(Startup.useSounds) Startup.soundManager.play("pos.mp3");

						//Stabilisco il turno successivo
						refModel.nextRound();
						//Stabilisco lo stato successivo
						if(refModel.getScacchiera().getPedine((byte)0) == 12
						&& refModel.getScacchiera().getPedine((byte)1) == 12)	refModel.stato=GameState.Select;
						//Aggiorno le info e la scacchiera
						refModel.updateInfo();
						if(refModel.getPlayers()[refModel.turno.ordinal()].isCPU())
						{
							CPU = new AI(1);
							CPU.start();
						}
					}
					else
						return -2;  //Pedine contigue
				}
				return 0;   //Campo già occupato

			case Select:
				if(!noMove())	//Controllo se può muovere almeno una pedina
				{
					if(!isBlocked(_i,_j))	//Controllo se la pedina scelta è bloccata
					{
						scacchiera.setSelected(_i, _j);
						if(Startup.useSounds) Startup.soundManager.play("sel.mp3");
						refModel.setState(GameState.Move);
					}
					if(refModel.getPlayers()[refModel.turno.ordinal()].isCPU()){
						CPU = new AI(1);
						CPU.start();
					}
					return 0;
				}
				else
				{	//Se non può muovere nemmeno una pedina passa automaticamente il turno
					if(Startup.useSounds) Startup.soundManager.play("nomove.mp3");
					refModel.nextRound();
					refModel.updateInfo();
					if(refModel.getPlayers()[refModel.turno.ordinal()].isCPU())
					{
						CPU = new AI(1);
						CPU.start();
					}
					return -3;  //Non ci sono pedine disponibili
				}

			case Move:
				if (	(scacchiera.getCampo(_i, _j) == 2) 	//Se il campo è vuoto
						&&	(	(Math.abs(scacchiera.getRigSel()-_i) == 1 && Math.abs(scacchiera.getColSel()-_j) == 0)
								||   (Math.abs(scacchiera.getRigSel()-_i) == 0 && Math.abs(scacchiera.getColSel()-_j) == 1)
							) //E se è adiacente al campo selezionato
					)
				{
					byte S =(byte)(turno.ordinal());
					refModel.setCampo(0, S, scacchiera.getRigSel(), scacchiera.getColSel());
					refModel.setCampo(1, S, _i, _j);
					if(isContigui(_i,_j))
					{
						refModel.setTrisInARow(TrisCount(_i, _j));
						refModel.setState(GameState.DelPed);
						if(Startup.useSounds) Startup.soundManager.play("tris.mp3");
					}else{
						if(Startup.useSounds) Startup.soundManager.play("pos.mp3");
						refModel.nextRound();
						refModel.setState(GameState.Select);
					}
				}else{
					if(scacchiera.getRigSel() == _i && scacchiera.getColSel() == _j )
					{
						scacchiera.setSelected((byte)-1,(byte)-1);
						refModel.setState(GameState.Select);
					}
				}
				if(refModel.getPlayers()[refModel.turno.ordinal()].isCPU())
				{
					CPU = new AI(1);
					CPU.start();
				}
				return 0;

			case DelPed:
				byte S =(byte)((turno.ordinal()+1)%2);
				refModel.setCampo(0, S, _i, _j);
				refModel.removeTris();
				if(refModel.getPlayers()[(byte)S].getCurrentPieces() <= 2)
				{
					if(Startup.useSounds) Startup.soundManager.play("win.mp3");
					refModel.setState(GameState.Win);
				}else{
					if(Startup.useSounds) Startup.soundManager.play("del.mp3");
					if(refModel.getTrisInARow() == 0)
					{
						refModel.nextRound();
						refModel.setState(GameState.Select);
					}else{
						refModel.setState(GameState.DelPed);
					}
				}
				if(refModel.getPlayers()[refModel.turno.ordinal()].isCPU())
				{
					CPU = new AI(1);
					CPU.start();
				}
				return 0;
		}
		return -1;  //Errore generale
	}

	public void resa()
	{
			if(Startup.useSounds) Startup.soundManager.play("win.mp3");
			refModel.nextRound();
			refModel.setState(GameState.Win);
	}

	public int TrisCount(byte _r, byte _c){
		int numTris = 0;
		byte[][] C = refModel.getScacchiera().getMatrix();
		byte S = (byte)(refModel.turno.ordinal());
		if(_r+1 < C.length && C[_r+1][_c] == S)
		{
			if(_r+2 < C.length && C[_r+2][_c] == S)
				numTris++;
			if(_r-1 >= 0 && C[_r-1][_c] == S)
				numTris++;
		}

		if(_r-1 > 0 && C[_r-1][_c] == S)
		{
			if(_r-2 >= 0 && C[_r-2][_c] == S)
				numTris++;
		}

		if(_c+1 < C[0].length && C[_r][_c+1] == S)
		{
			if(_c+2 < C[0].length && C[_r][_c+2] == S)
				numTris++;
			if(_c-1 >= 0 && C[_r][_c-1] == S)
				numTris++;
		}

		if(_c-1 > 0 && C[_r][_c-1] == S)
		{
			if(_c-2 >= 0 && C[_r][_c-2] == S)
				numTris++;
		}
		return numTris;
	}

	public boolean noMove()
	{
		byte[][] m = refModel.getScacchiera().getMatrix();
		byte S = (byte)(refModel.turno.ordinal());
		for(byte i=0; i < m.length; i++)
			for(byte j=0; j < m[i].length; j++)
				if((m[i][j] == S) && (!isBlocked(i,j)))
					return false;
		return true;
	}

	public boolean isBlocked(byte r,byte c)
	{
		byte[][] m = refModel.getScacchiera().getMatrix();
		if( ((r+1) < m.length && m[r+1][c] == 2) ||
			((c+1) < m[0].length && m[r][c+1] == 2) ||
			((r-1) >= 0 && m[r-1][c] == 2) ||
			((c-1) >= 0 && m[r][c-1] == 2) )
				return false;
		return true;
	}

	public void startGame()
	{
		if(Startup.useSounds) Startup.soundManager.stop("main.mp3");

		//Setto una nuova partita (Setto stato(posizionamento) e genero turno(random))
		refModel.newGame();
		nextGame();
	}

	public void nextGame()
	{
		//reset della matrice
		refModel.clearCampi();

		//Vado al prossimo giocatore
		refModel.nextGameRound();
		if(Startup.useSounds) Startup.soundManager.play("gong.mp3");
		if(refModel.getPlayers()[refModel.turno.ordinal()].isCPU()){
			CPU = new AI(1);
			CPU.start();
		}
	}

	public boolean isContigui(byte _r,byte _c)
	{
		byte[][] C = refModel.getScacchiera().getMatrix();
		byte S = (byte)(refModel.turno.ordinal());
		if(_r+1 < C.length && C[_r+1][_c] == S)
		{
			if(_r+2 < C.length && C[_r+2][_c] == S)
				return true;
			if(_r-1 >= 0 && C[_r-1][_c] == S)
				return true;
		}

		if(_r-1 > 0 && C[_r-1][_c] == S)
		{
			if(_r-2 >= 0 && C[_r-2][_c] == S)
				return true;
		}

		if(_c+1 < C[0].length && C[_r][_c+1] == S)
		{
			if(_c+2 < C[0].length && C[_r][_c+2] == S)
				return true;
			if(_c-1 >= 0 && C[_r][_c-1] == S)
				return true;
		}

		if(_c-1 > 0 && C[_r][_c-1] == S)
		{
			if(_c-2 >= 0 && C[_r][_c-2] == S)
				return true;
		}
		return false;
	}
}

