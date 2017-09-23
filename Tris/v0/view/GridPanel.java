package javaGames.Tris.v0.view;

import javaGames.Tris.v0.controller.*;
import javaGames.Tris.v0.model.*;
import javaGames.Tris.v0.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GridPanel
	extends JPanel
	implements ActionListener
{
	public JButton[][] cmdGrid;

	public GridPanel()
	{
		super();

		//Setup del JPanel griglia
		setLocation(0,0);
		setSize(710,440);
		setLayout(new GridLayout(5,6,3,3));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(25,25,0,0));

		//Setup della matrice JButton
		cmdGrid = new JButton[5][6];
		for(byte i=0; i < cmdGrid.length; i++)
			for(byte j=0; j < cmdGrid[i].length; j++)
			{
				cmdGrid[i][j] = new JButton();
				cmdGrid[i][j].setOpaque(true);
				cmdGrid[i][j].setContentAreaFilled(false);
				cmdGrid[i][j].setFont(new Font("Serif", Font.BOLD, 40)); //Setup del font
				cmdGrid[i][j].setHorizontalTextPosition(0); //Setta lo sfondo e il testo su due layer diversi
				cmdGrid[i][j].addActionListener(this);
				cmdGrid[i][j].setForeground(Color.LIGHT_GRAY);
				cmdGrid[i][j].setBackground(null);
				add(cmdGrid[i][j]);
			}
	}

	@Override
	public void ActionPerformed(ActionEvent e)
	{
		//Controllo se è il turno del player
		if()
		{
			if(e instanceof JButton)
			{

			}
		}
	}
}