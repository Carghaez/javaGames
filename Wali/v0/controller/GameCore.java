package javaGames.Wali.v0.controller;

import javaGames.Wali.v0.model.*;
import javaGames.Wali.v0.model.GameData.GameRound;
import javaGames.Wali.v0.model.GameData.GameState;
import javaGames.Wali.v0.*;

public class GameCore
{
	private GameData refModel;

	public GameCore(GameData d)
	{
		refModel = d;
	}

	public int press(byte _i,byte _j)
	{
		GameRound turno = refModel.turno;
		BoardData scacchiera = refModel.getScacchiera();
		//Controllo delle operazioni in base allo stato della partita
		switch(refModel.stato)
		{
			case Pos:
				//Controllo se il campo è vuoto
				if(refModel.getScacchiera().getCampo(_i,_j) == 2)
				{
					if(!isContigui(_i,_j))	//Controllo se esistono più di due pedine contigue
					{
						//Setto e aggiorno la pedina
						byte S = (byte)((turno == GameRound.Player1)? 0 : 1);
						refModel.getScacchiera().setCampo(S, _i, _j);
						refModel.AggiornaPedine(S);
						//Stabilisco il turno successivo
						nextRound();
						//Stabilisco lo stato successivo
						if(refModel.getScacchiera().getPedine((byte)0) == 12
						&& refModel.getScacchiera().getPedine((byte)1) == 12)	refModel.stato=GameState.Select;
						//Aggiorno le info e la scacchiera
						refModel.updateInfo();
						refModel.getScacchiera().Refresh();
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
						refModel.stato=GameState.Move;
						refModel.getScacchiera().setSelected(_i, _j);
						refModel.updateInfo();
					}
					return 0;
				}
				else
				{	//Se non può muovere nemmeno una pedina passa automaticamente il turno
					nextRound();
					refModel.updateInfo();
					refModel.getScacchiera().Refresh();
					return -3;  //Non ci sono pedine disponibili
				}

			case Move:
				if((refModel.getScacchiera().getCampo(_i,_j) == 2) 	//Se il campo è vuoto
					&&	(Math.abs(refModel.getScacchiera().getRigSel()-_i) == 1
						|| Math.abs(scacchiera.getColSel()-_j) == 1)) //E se è adiacente al campo selezionato
				{
					byte S =(byte)((turno == GameRound.Player1)? 0 : 1);
					refModel.getScacchiera().setCampo((byte)2, scacchiera.getRigSel(), scacchiera.getColSel());
					refModel.getScacchiera().setCampo(S, _i, _j);
					if(isContigui(_i,_j)){
						refModel.stato = GameState.DelPed;
					}else{
						nextRound();
						refModel.stato = GameState.Select;
					}
					refModel.updateInfo();
					refModel.getScacchiera().Refresh();
				}
				else
				{
					if(refModel.getScacchiera().getRigSel() == _i && refModel.getScacchiera().getColSel() == _j ){
						refModel.getScacchiera().setSelected((byte)-1,(byte)-1);
						refModel.stato = GameState.Select;
						refModel.updateInfo();
						refModel.getScacchiera().Refresh();
					}
				}
				return 0;
			case DelPed:
				byte S =(byte)((turno == GameRound.Player1)? 0 : 1);
				if(refModel.getPlayers()[(byte)((S+1)%2)].getPedine() <= 3)
				{
					refModel.stato=GameState.Win;
					refModel.getPlayers()[S].addVittoria();
				}else{
					refModel.stato=GameState.Select;
					nextRound();
				}
				refModel.getScacchiera().setCampo((byte)2,_i, _j);
				refModel.AggiornaPedine((byte)((S+1)%2));
				refModel.updateInfo();
				return 0;
		}
		return -1;  //Errore generale
	}

	public boolean noMove()
	{
		byte[][] m = refModel.getScacchiera().getMatrix();
		byte S = (byte)((refModel.turno==GameRound.Player1)?0:1);
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

	public void start()
	{
		Startup.soundManager.play("m_01.mp3");
		//Settaggio stato in base a chi inizia la partita
		refModel.stato = GameState.Pos;
		nextGame();
		if(refModel.getWhoPlayRound() == 0)
			refModel.turno=GameRound.Player1;
		else
			if(!refModel.getPlayers()[1].isCPU())
				refModel.turno=GameRound.Player2;
			else
				refModel.turno=GameRound.CPU;

		//reset della matrice
		refModel.getScacchiera().resetCampi();

		//Settaggio pedine dei giocatori
		for(byte i=0; i < refModel.getPlayers().length; i++)
			refModel.AggiornaPedine(i);

		//Update di info in base allo stato
		refModel.updateInfo();
		//Aggiorna matrice
		//refModel.getScacchiera().Refresh();
	}

	public void nextRound()
	{
		if(refModel.turno == GameRound.Player1)
		{
			refModel.setWhoPlayRound((byte)1);
			if(refModel.getPlayers()[1].isCPU())
				refModel.turno = GameRound.CPU;
			else
				refModel.turno = GameRound.Player2;
		}else{
			refModel.turno = GameRound.Player1;
			refModel.setWhoPlayRound((byte)0);
		}
	}

	public int nextGame()
	{
		refModel.setWhoPlayGame((byte)((refModel.getWhoPlayGame()+1)%2));
		refModel.setWhoPlayRound(refModel.getWhoPlayRound());
		return refModel.getWhoPlayGame();
	}

	public boolean isContigui(byte _r,byte _c)
	{
		byte[][] C = refModel.getScacchiera().getMatrix();
		byte S = (byte)((refModel.turno == GameRound.Player1)?0:1);
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
			if(_r+1 < C.length && C[_r+1][_c] == S)
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
			if(_c+1 < C[0].length && C[_r][_c+1] == S)
				return true;
		}
		return false;
	}
}

