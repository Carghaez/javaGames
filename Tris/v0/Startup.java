package javaGames.Tris.v0;

import javaGames.Tris.v0.controller.*;
import javaGames.Tris.v0.model.*;
import javaGames.Tris.v0.view.*;

public class Startup
{
	//Allocazione del pattern MVC (Model,View,Controller)
	public static GameData dati;    // Model
	public static GUI window;       // View
	public static Core core;        // Controller

	public static void main(String[] args)
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				creaFinestraPrincipale();
			}
		});
	}

	public static void creaFinestraPrincipale()
	{
		window = new GUI();
	}
}