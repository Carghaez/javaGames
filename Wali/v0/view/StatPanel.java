package javaGames.Wali.v0.view;

import javaGames.Wali.v0.*;
import javaGames.Wali.v0.model.*;
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
		setBackground(Color.LIGHT_GRAY);
		//Setup delle Label
		//Innesta l'ascolto dei dati del giocatore alle due label
		lblPl = new StatLabel[2];
		for(byte i=0;i<Startup.dati.getPlayers().length;i++)
		{
			lblPl[i] = new StatLabel();
			Startup.dati.getPlayers()[i].addObserver(lblPl[i]);
			add(lblPl[i]);
		}
	}
}

class StatLabel extends JLabel implements Observer
{
	public StatLabel()
	{
		super();
		setFont(new Font("Serif", Font.BOLD, 25));
		setBorder(BorderFactory.createEmptyBorder(/*top=*/0,/*left=*/30,/*bottom=*/0,/*right=*/30));
	}

	@Override
	public void update(Observable o, Object arg)
	{
		this.setText(((PlayerData)o).getNick()+" (Simbolo: "+((PlayerData)o).getSymbol()+", Pedine: "+arg+", Vittorie: "+((PlayerData)o).getVittorie()+")");
	}
}
