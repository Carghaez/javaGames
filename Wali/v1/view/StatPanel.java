package javaGames.Wali.v1.view;

import javaGames.Wali.v1.*;
import javaGames.Wali.v1.model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class StatPanel
	extends JPanel
{
	public StatLabel lblPl[];

	public StatPanel()
	{
		super();
		//Setup JPanel Statistiche
		setLayout(new GridLayout(2,1));
		setOpaque(true);
		setBackground(null);
		setOpaque(false);

		//Setup delle Label
		//Innesta l'ascolto dei dati del giocatore alle due label
		lblPl = new StatLabel[2];
		for(byte i=0; i < lblPl.length; i++)
		{
			lblPl[i] = new StatLabel();
			Startup.dati.getPlayers()[i].addObserver(lblPl[i]);
			add(lblPl[i]);
		}
	}
}

class StatLabel
	extends JLabel
	implements Observer
{
	public StatLabel()
	{
		super();
		setFont(Startup.fntAhnbergHand);
		setForeground(new Color(40, 60, 40));
		setBorder(BorderFactory.createEmptyBorder(/*top=*/0,/*left=*/30,/*bottom=*/0,/*right=*/30));
		setOpaque(false);
	}

	@Override
	public void update(Observable o, Object arg)
	{
		String x="";
		String cpu="";

		if(Startup.useSymbols)
			x="Simbolo";
		else
			x="Colore";

		if(((PlayerData)o).isCPU())
			cpu="=>CPU";

		this.setText(((PlayerData)o).getNick()+" ("+x+": "+((PlayerData)o).getSymbol()+", Pedine: "+((PlayerData)o).getCurrentPieces()+", Vittorie: "+((PlayerData)o).getVittorie()+")"+cpu);

	}
}
