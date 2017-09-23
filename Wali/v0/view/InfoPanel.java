package javaGames.Wali.v0.view;

import javaGames.Wali.v0.*;
import javaGames.Wali.v0.model.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class InfoPanel
	extends JPanel
	implements Observer, ActionListener
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
		setBackground(Color.lightGray);
		setBorder(BorderFactory.createEmptyBorder(0,10,0,15));

		//Setup JLabel info
		lblInfo=new JLabel();
		lblInfo.setFont(new Font("Serif", Font.ITALIC, 18));
		lblInfo.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

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
			Startup.core.start();
			((JButton)e.getSource()).setText("Abbandona");
		} else {
			if(s.equals("Abbandona")) {
				int i = JOptionPane.showConfirmDialog(null, "Sei sicuro? Se abbandoni la partita vincera' l'avversario!");
				switch(i)
				{
					case 0: //Ha premuto si
						//Termina la partita con la vittoria dell'avversario
						Startup.frmGame.setVisible(false);
						break;

					case 1: //Ha premuto no
						break;

					case 2: //Ha annullato
						break;
				}
			} else {
				JOptionPane.showMessageDialog(null, "pulsante da implementare");
			}
		}
	}
}
