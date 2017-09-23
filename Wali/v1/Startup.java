package javaGames.Wali.v1;

import javaGames.Wali.v1.controller.*;
import javaGames.Wali.v1.model.*;
import javaGames.Wali.v1.view.*;
import javaGames.Wali.v1.engine.*;

import java.net.*;
import javax.swing.*;
import java.awt.Font;
import java.awt.Graphics;
import java.io.*;

public class Startup
{
	//Costanti
	public final static String pathMain = "javaGames/Wali/v1/view/resources/";
	public final static URL urlSfondo = Startup.class.getClassLoader().getResource(pathMain+"images/Background2.jpg");
	public final static URL urlBackWali = Startup.class.getClassLoader().getResource(pathMain+"images/BackWali.png");
	public final static URL urlBottone = Startup.class.getClassLoader().getResource(pathMain+"images/BackButton.jpg");
	public final static URL urlAbout = Startup.class.getClassLoader().getResource(pathMain+"images/about.png");

	public final static URL urlP1 =  Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_01.png");
	public final static URL urlP2 = Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_02.png");
	public final static URL urlP3 = Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_03.png");
	public final static URL urlP4 = Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_04.png");
	public final static URL urlP5 = Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_05.png");
	public final static URL urlP6 = Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_06.png");
	public final static URL urlP7 = Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_07.png");
	public final static URL urlP8 = Startup.class.getClassLoader().getResource(pathMain+"images/pieces/p_08.png");
	public final static ImageIcon imgPulsante = new ImageIcon(urlBottone);
	public final static SoundManager soundManager = new SoundManager();
	public static Font fntAhnbergHand;

	//Allocazione del pattern MVC (Model,View,Controller)
	public static GameData dati;    //Model
	public static GUI window;  		//View
	public static GameCore core;    //Controller
	public static boolean useSymbols = true;
	public static boolean useSounds = true;

	public static void main(String[] args)
	{
		//carico un font esterno
		loadStatFont();
		//Carico i suoni
		addSound();
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(
			new Runnable()
			{
				@Override
				public void run() { creaFinestraPrincipale(); }
			}
		);
	}

	public static void creaFinestraPrincipale()
	{
		window = new GUI();
		soundManager.play("main.mp3");
	}

	public static void addSound()
	{
		soundManager.add(pathMain+"BGM/main.mp3");
		soundManager.add(pathMain+"BGM/tris.mp3");
		soundManager.add(pathMain+"BGM/gong.mp3");
		soundManager.add(pathMain+"BGM/pos.mp3");
		soundManager.add(pathMain+"BGM/win.mp3");
		soundManager.add(pathMain+"BGM/del.mp3");
		soundManager.add(pathMain+"BGM/sel.mp3");
		soundManager.add(pathMain+"BGM/nomove.mp3");
	}

	public static void loadStatFont()
	{
		try
		{
			final URL urlFont = Startup.class.getClassLoader().getResource(pathMain+"fonts/AhnbergHand.TTF");
			final InputStream isFont = urlFont.openStream();
			fntAhnbergHand = Font.createFont(Font.TRUETYPE_FONT , isFont);
			isFont.close();
			//imposto la dimensione
			float size = 20.0f;
			fntAhnbergHand = fntAhnbergHand.deriveFont(size);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,e);
		}
	}
}