package javaGames.Wali.v1.view;

import javaGames.Wali.v1.controller.*;
import javaGames.Wali.v1.*;
import javaGames.Wali.v1.model.*;
import javaGames.Wali.v1.model.GameData.GameRound;
import javaGames.Wali.v1.model.GameData.GameState;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class GamePanel
	extends JPanel
{
	private StatPanel pnlStat;
	private ContainerPanel pnlContenitore;

	public GamePanel()
	{
		super();
		//Setup del pannello
		setSize(800,600);
		setLayout(new BorderLayout());
		setLocation(0,0);
		setOpaque(false);
		//Settaggio JPanel Stat
		pnlStat=new StatPanel();

		//Settaggio JPanel Contenitore
		pnlContenitore = new ContainerPanel();


		//Inserimento nel JPanel principale
		add(pnlStat,BorderLayout.NORTH);
		add(pnlContenitore,BorderLayout.CENTER);
	}

	public StatPanel getpnlStat(){ return pnlStat; }
	public ContainerPanel getpnlContenitore(){ return pnlContenitore;}
}

class ContainerPanel
	extends JPanel
{
	private BackPanel pnlBack;
	private InfoPanel pnlInfo;

	public ContainerPanel()
	{
		super();
		//Setup JPanel Contenitore
		setLayout(null);
		setBackground(null);
		setOpaque(false);

		//Setup JPanel Background (conterrà lo sfondo)
		pnlBack=new BackPanel();

		//Setup JPanel Info
		pnlInfo = new InfoPanel();

		add(pnlBack);
		add(pnlInfo);
	}
	public InfoPanel getpnlInfo(){ return pnlInfo; }
}

class BackPanel
	extends JPanel
	implements Observer
{
	private Image Sfondo;
	private GridPanel pnlGrid;

	public BackPanel()
	{
		super();

		//Setup del JPanel
		setSize(740,465);
		setLayout(null);
		setLocation(30,0);
		setBackground(null);
		setOpaque(false);

		if(Startup.useSymbols){
			//Setting dell'immagine di sfondo
			Toolkit tk= Toolkit.getDefaultToolkit();
			Sfondo = tk.getImage(Startup.urlBackWali);
			MediaTracker mt=new MediaTracker(this);
			mt.addImage(Sfondo,1);
			try { mt.waitForID(1); }
			catch (InterruptedException e){}
		}
		//Setup JPanel griglia
		pnlGrid = new GridPanel();
		add(pnlGrid);

		//Innesta l'ascolto della scacchiera al contenitore della griglia
		Startup.dati.getScacchiera().addObserver(this);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(Sfondo,0,0,null);   // Immagine,posizione,posizione,oggetto a cui notificare il caricamento
	}

	@Override
	public void update(Observable o, Object arg)
	{
	   pnlGrid.Refresh((byte[][])arg); //Aggiorno tutti i pulsanti
	}
}
