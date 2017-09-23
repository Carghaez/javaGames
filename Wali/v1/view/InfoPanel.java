package javaGames.Wali.v1.view;

import javaGames.Wali.v1.*;
import javaGames.Wali.v1.model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class InfoPanel
	extends JPanel
	implements Observer,ActionListener
{
	public JLabel lblInfo;
	public JButton cmdAbbandona;
	public JButton cmdTermina;

	public InfoPanel()
	{
		super();

		//Setup JPanel info
		setSize(800,30);
		setLocation(0,470);
		setLayout(new BorderLayout());
		setBackground(null);
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(0,10,0,15));

		//Setup JLabel info
		lblInfo=new JLabel();
		lblInfo.setFont(new Font("Serif", Font.ITALIC, 20));
		lblInfo.setForeground(new Color(15, 30, 15));
		lblInfo.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
		lblInfo.setOpaque(false);

		//Setup JButton ecc.
		cmdTermina = new JButton("Termina");
		cmdTermina.addActionListener(this);
		cmdAbbandona = new JButton("Abbandona");
		cmdAbbandona.addActionListener(this);
		add(lblInfo,BorderLayout.CENTER);
		add(cmdAbbandona,BorderLayout.WEST);
		add(cmdTermina,BorderLayout.EAST);

		//Innesto l'ascolto dei dati al pannello info
		Startup.dati.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg)
	{
		lblInfo.setText(arg.toString());
		if(((String)arg).charAt(((String)arg).length()-1)=='!')
		{
			cmdAbbandona.setText("Rivincita");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String s= ((JButton)e.getSource()).getText();
		if(s.equals("Rivincita"))
		{
			Runtime r = Runtime.getRuntime();
			r.gc();
			Startup.core.nextGame();
			((JButton)e.getSource()).setText("Abbandona");
		}else{
			if(s.equals("Abbandona")){
				if(!Startup.dati.getPlayers()[Startup.dati.turno.ordinal()].isCPU())
				{
					int i = JOptionPane.showConfirmDialog(Startup.window, "Sei sicuro? Se abbandoni questa partita vincera' l'avversario!");
					switch(i)
					{
						case 0: //Ha premuto si
							//Visualizzare JOptionPane con vittoria dell'avversario + musica vittoria
							//JOptionPane.showMessageDialog(Startup.window, Startup.dati.getNameOfNextPlayer() + " ha vinto!");
							Startup.core.resa();
							break;

						case 1: //Ha premuto no
							break;

						case 2: //Ha annullato
							break;
					}
				}
			}else{
				//JOptionPane.showMessageDialog(null, "pulsante da implementare");
				//Chiude il gioco
				Startup.window.closeGamePanel();
				if(Startup.useSounds) Startup.soundManager.play("main.mp3");
			}
		}
	}
}
