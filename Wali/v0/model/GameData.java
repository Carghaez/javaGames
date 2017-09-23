package javaGames.Wali.v0.model;

import java.util.Observable;

public class GameData
	extends Observable
{
	public final /* fantasy */ static String[] simbolo = { "X", "O", "D","E","F","A","Y","B" };
	public final static String[] infoSTR = {
		" posiziona una pedina",
		" seleziona la pedina da spostare",
		" sposta pedina in una casella contigua",
		" ha fatto tris! Elimina una pedina avversaria",
		" ha vinto la partita! Complimenti!"
	};
	public enum GameState {
			Pos,
			Select,
			Move,
			DelPed,
			Win,
	}
	public enum GameRound {
		Player1,
		Player2,
		CPU
	}
	public GameState stato;
	public GameRound turno;

	private PlayerData[] giocatore;
	private BoardData scacchiera;
	private byte WhoPlayRound;
	private byte WhoPlayGame;

	public GameData(String N,String X,boolean CPU,String _N,String _X,boolean _CPU)
	{
		giocatore = new PlayerData[2];
		giocatore[0] = new PlayerData(N,X,CPU);
		giocatore[1] = new PlayerData(_N,_X,_CPU);
		scacchiera = new BoardData();
		WhoPlayGame = ((byte)(Math.random()*2));
		WhoPlayRound = WhoPlayGame;
	}

	public void updateInfo()
	{
		String stringa = "";
		switch(turno)
		{
			case Player1:
				stringa=giocatore[0].getNick();
				break;

			case Player2:
				stringa=giocatore[1].getNick();
				break;

			case CPU:
				stringa="Intelligenza Artificiale";
				break;
		}
		stringa+=infoSTR[stato.ordinal()];
		setChanged();
		notifyObservers(stringa);
	}

	public void AggiornaPedine(byte o)
	{
		giocatore[o].setPedine(scacchiera.getPedine(o));
	}

	public BoardData getScacchiera(){ return scacchiera; }
	public PlayerData[] getPlayers(){ return giocatore; }
	public byte getWhoPlayRound(){ return WhoPlayRound; }
	public byte getWhoPlayGame(){ return WhoPlayGame; }
	public void setWhoPlayRound(byte x){WhoPlayRound=x; }
	public void setWhoPlayGame(byte x){WhoPlayGame=x; }
}