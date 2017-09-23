package javaGames.Wali.v0;

import javaGames.Wali.v0.controller.*;
import javaGames.Wali.v0.model.*;
import javaGames.Wali.v0.view.*;
import javaGames.Wali.v0.engine.*;
import java.net.*;
import javax.swing.*;

public class Startup
{
	//Costanti
	public final static URL urlSfondo = FileUtils.getURL("javaGames/Wali/v0/view/resources/images/BackWali.jpg");
	public final static URL urlBottone = FileUtils.getURL("javaGames/Wali/v0/view/resources/images/BackButton.jpg");
	public final static ImageIcon imgPulsante = new ImageIcon(urlBottone);
	public final static SoundManager soundManager = new SoundManager();

	//Allocazione finesta di configurazione iniziale
	public static ConfigFrame frmConfig;

	//Allocazione del pattern MVC (Model,View,Controller)
	public static GameData dati;    //Model
	public static GameGUI frmGame;  //View
	public static GameCore core;    //Controller

	public static void main(String[] args)
	{
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		soundManager.add("javaGames/Wali/v0/view/resources/BGM/m_01.mp3");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { creaConfigFrame(); }
		});
	}

	public static void creaConfigFrame()
	{
		frmConfig = new ConfigFrame();
	}

	public static void creaGameFrame()
	{
		frmGame = new GameGUI(dati);
	}
}
