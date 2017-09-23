package javaGames.Halma.v0.view;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.model.*;
import javaGames.Halma.v0.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public final class GUI
	extends JFrame
	implements ActionListener
{
	BoardPanel panel;
	JRadioButton rd1player1,rd2player1,rd1player2,rd2player2;
	JLabel info,player1,player2;
	JButton cmdReset,cmdNext;


	public GUI()
	{
		super("Halma");
		//settaggio finestra
		this.setLocation(100, 10);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);

		//settaggio controlli
		panel = new BoardPanel();
		panel.setVisible(true);
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

		info=new JLabel("Attendere");
		info.setSize(300,35);
		info.setLocation(20,495);

		cmdReset = new JButton("Reset mossa");
		cmdReset.setSize(150,30);
		cmdReset.setLocation(535,450);
		cmdReset.addActionListener(this);
		cmdReset.setEnabled(false);

		cmdNext = new JButton("Passa il turno");
		cmdNext.setSize(150,30);
		cmdNext.setLocation(535,500);
		cmdNext.addActionListener(this);
		cmdNext.setEnabled(false);

		//JRadioButton
		rd1player1 = new JRadioButton("Giocatore");
		rd1player1.setOpaque(false);
		rd1player1.setActionCommand("g1");
		rd1player1.setSelected(true);
		rd1player1.setSize(100,20);
		rd1player1.setLocation(535,100);
		rd1player1.addActionListener(this);

		rd2player1 = new JRadioButton("CPU");
		rd2player1.setOpaque(false);
		rd2player1.setActionCommand("c1");
		rd2player1.setSize(100,20);
		rd2player1.setLocation(535,150);
		rd2player1.addActionListener(this);

		player1=new JLabel("Giocatore " + Startup.dati.Player[0].simbolo);
		player1.setSize(100,20);
		player1.setLocation(535,50);

		// Crea il ButtonGroup e registra i RadioButton
		ButtonGroup groupPlayer1 = new ButtonGroup();
		groupPlayer1.add(rd1player1);
		groupPlayer1.add(rd2player1);


		rd1player2 = new JRadioButton("Giocatore");
		rd1player2.setOpaque(false);
		rd1player2.addActionListener(this);
		rd1player2.setActionCommand("g2");
		rd1player2.setSelected(true);
		rd1player2.setSize(100,20);
		rd1player2.setLocation(535,300);
		rd1player2.addActionListener(this);

		rd2player2 = new JRadioButton("CPU");
		rd2player2.setOpaque(false);
		rd2player2.addActionListener(this);
		rd2player2.setActionCommand("c2");
		rd2player2.setSize(100,20);
		rd2player2.setLocation(535,350);

		player2=new JLabel("Giocatore " + Startup.dati.Player[1].simbolo);
		player2.setSize(100,20);
		player2.setLocation(535,250);

		// Crea il ButtonGroup e registra i RadioButton
		ButtonGroup groupPlayer2 = new ButtonGroup();
		groupPlayer2.add(rd1player2);
		groupPlayer2.add(rd2player2);

		//aggancio alla GUI
		this.getContentPane().add(panel);

		this.getContentPane().add(rd1player1);
		this.getContentPane().add(rd2player1);
		this.getContentPane().add(player1);

		this.getContentPane().add(rd1player2);
		this.getContentPane().add(rd2player2);
		this.getContentPane().add(player2);

		this.getContentPane().add(info);

		this.getContentPane().add(cmdReset);
		this.getContentPane().add(cmdNext);

		this.setVisible(true);
	}

	public void refresh()
	{
		switch(Startup.dati.stato)
		{
			case 0:
				info.setText("Seleziona pedina da spostare");
				cmdNext.setEnabled(false);
				cmdReset.setEnabled(false);
				break;
			case 1:
				info.setText("Seleziona casella di destinazione");
				cmdNext.setEnabled(false);
				cmdReset.setEnabled(false);
				break;
			case 2:
				info.setText("Seleziona salto o passa il turno");
				cmdNext.setEnabled(true);
				cmdReset.setEnabled(true);
				break;
			case 3:
				info.setText("Resetta mossa o passa il turno");
				cmdNext.setEnabled(true);
				cmdReset.setEnabled(true);
				break;
		}
		panel.refresh();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if((e.getSource() instanceof  JButton) && ((JButton)e.getSource()==cmdNext)){
			Startup.core.nextTurn();
		}
		if((e.getSource() instanceof  JButton) && ((JButton)e.getSource()==cmdReset)){
			Startup.core.reset();
		}
		if(e.getSource()instanceof JRadioButton){
			if(((JRadioButton)e.getSource()) == rd1player1){
				Startup.dati.Player[0].isCpu=false;
			}
			if(((JRadioButton)e.getSource()) == rd2player1){
				Startup.dati.Player[0].isCpu=true;
				if(Startup.dati.turno==0)Startup.core.makeAI();
			}
			if(((JRadioButton)e.getSource()) == rd1player1){
				Startup.dati.Player[1].isCpu=false;
			}
			if(((JRadioButton)e.getSource()) == rd2player2){
				Startup.dati.Player[1].isCpu=true;
				if(Startup.dati.turno==1)Startup.core.makeAI();
			}
		}
	}
}