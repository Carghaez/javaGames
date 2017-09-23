package javaGames.Wali.v1.model;

import javaGames.Wali.v1.*;
import javax.swing.*;
import java.util.Observable;

public class GameData extends Observable {
	//Proprietà  pubbliche
	public final /* fantasy */ static String[] simbolo = { "X", "O","A","B","C","D","E","F","G","H","J","I","L","M","N","P","Q","R","S","T","U","W","K","Y","Z" };
	public final static ImageIcon[] icona = {
												new ImageIcon(Startup.urlP1),
												new ImageIcon(Startup.urlP2),
												new ImageIcon(Startup.urlP3),
												new ImageIcon(Startup.urlP4),
												new ImageIcon(Startup.urlP5),
												new ImageIcon(Startup.urlP6),
												new ImageIcon(Startup.urlP7),
												new ImageIcon(Startup.urlP8)
											};
	public final static String[] nomiIcone = {
												"Grigio",
												"Viola",
												"Rosso",
												"Azzurro",
												"Verde",
												"Blu",
												"Marrone",
												"Giallo"
											};
	public final static String[] infoSTR={
		" posiziona una pedina",
		" seleziona la pedina da spostare",
		" sposta pedina in una casella contigua",
		" ha vinto la partita! Complimenti!",
		" ha fatto tris! Elimina una pedina avversaria"
	};

	public enum GameState{
			Pos,
			Select,
			Move,
			Win,
			DelPed,
	}

	public enum GameRound{
		Player1,
		Player2
	}

	public GameState stato;
	public GameRound turno;
	public GameRound partita;
	//Proprieta'  private
	private PlayerData[] giocatore;
	private BoardData scacchiera;
	private int TrisInARow;
	private int numTrisInARow;

	//Costruttore per uso SIMBOLI
	public GameData(int i, String N,String X,boolean CPU,
					int _i,String _N,String _X,boolean _CPU)
	{
		giocatore = new PlayerData[2];
		giocatore[0] = new PlayerData(i, N, X, CPU);
		giocatore[1] = new PlayerData(_i, _N, _X, _CPU);
		scacchiera = new BoardData();
	}
	//Costruttore per uso ICONE
	public GameData(int i, String N,ImageIcon I,String O,boolean CPU,
					int _i, String _N,ImageIcon _I,String _O,boolean _CPU)
	{
		giocatore = new PlayerData[2];
		giocatore[0] = new PlayerData(i, N, I, O, CPU);
		giocatore[1] = new PlayerData(_i, _N, _I, _O, _CPU);
		scacchiera = new BoardData();
	}

	public void updateInfo()
	{
		scacchiera.Refresh();
		setChanged();
		if(stato == GameState.DelPed)
		{
			notifyObservers(giocatore[turno.ordinal()].getNick()+" ha fatto "+numTrisInARow+" tris! Elimina "+TrisInARow+" pedina/e avversaria/e");
		}else{
			notifyObservers(giocatore[turno.ordinal()].getNick()+infoSTR[stato.ordinal()]);
		}
	}

	public BoardData getScacchiera(){ return scacchiera; }
	public PlayerData[] getPlayers(){ return giocatore; }
	public int getTrisInARow(){	return TrisInARow; }
	public void setTrisInARow(int x){ numTrisInARow=TrisInARow=x; }
	public void removeTris(){ TrisInARow--; }

	public String getNameOfNextPlayer()
	{
		return giocatore[(turno.ordinal()+1)%2].getNick();
	}

	public void nextRound()
	{
		turno = (turno == GameRound.Player1)? GameRound.Player2 : GameRound.Player1;
	}

	public void nextGameRound()
	{
		turno = partita = (partita == GameRound.Player1)? GameRound.Player2 : GameRound.Player1;
		setState(GameState.Pos);
	}

	public void newGame()
	{
		GameRound[] v = new GameRound[2];
		v[0] = GameRound.Player1;
		v[1] = GameRound.Player2;
		partita = v[((int)(Math.random()*2))];
		giocatore[0].clearAllData();
		giocatore[1].clearAllData();
	}

	public void setState(GameState x){
		stato = x;
		if(x == GameState.Win) giocatore[turno.ordinal()].addVittoria();
		updateInfo();
	}

	public void setCampo(int op,byte iPl, byte r, byte c)
	{
		switch(op)
		{
			case 0:
				//Rimuovo pedina
				giocatore[iPl].removePiece(r, c);
				scacchiera.setCampo((byte)2, r, c);
			break;

			case 1:
				//Aggiungo pedina
				giocatore[iPl].addPiece(r,c);
				scacchiera.setCampo(iPl, r, c);
			break;
		}
	}

	public void clearCampi()
	{
		scacchiera.resetCampi();
		giocatore[0].removeAllPieces();
		giocatore[1].removeAllPieces();
	}
}