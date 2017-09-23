package javaGames.Halma.v0;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.model.*;
import javaGames.Halma.v0.view.*;

public class Startup
{
	public final static GameData dati = new GameData();
	public final static Core core = new Core();
	public static GUI finestra;

	public static void main(String[] Args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				initGUI();
			}
		});
	}

	public static void initGUI()
	{
		finestra = new GUI();
	}
}