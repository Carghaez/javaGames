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

public class GridPanel extends JPanel implements ActionListener
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

	public void Refresh(byte[][] m)
	{
		for(byte i=0;i<cmdGrid.length;i++)
			for(byte j=0;j<cmdGrid[i].length;j++)
				switch(Startup.dati.stato)
				{
					case Pos:
						if(m[i][j]==2)
							setPulsante(cmdGrid[i][j],true);
						else
							if(useSymbols())
								setPulsante(cmdGrid[i][j],true,Startup.dati.getPlayers()[m[i][j]].getSymbol());
							else
								setPulsante(cmdGrid[i][j], true, Startup.dati.getPlayers()[m[i][j]].getIcon());
						break;

					case Select:
						if(m[i][j]==2){
							setPulsante(cmdGrid[i][j],false);
						}else{
							boolean isEn=((m[i][j]==0 && Startup.dati.turno==GameRound.Player1)||(m[i][j]==1 && !(Startup.dati.turno==GameRound.Player1)))?true:false;
							if(useSymbols())
								setPulsante(cmdGrid[i][j],isEn,Startup.dati.getPlayers()[m[i][j]].getSymbol());
							else
								setPulsante(cmdGrid[i][j],isEn,Startup.dati.getPlayers()[m[i][j]].getIcon());
						}
						break;

					case Move:
						byte R=Startup.dati.getScacchiera().getRigSel();
						byte C=Startup.dati.getScacchiera().getColSel();
						if(i==R && j==C)
						{
							cmdGrid[i][j].setForeground(Color.YELLOW);
							if(!Startup.useSymbols)	cmdGrid[i][j].setText("!");
							cmdGrid[i][j].setEnabled(true);
							if(R < m.length-1 && m[R+1][C] == 2)	cmdGrid[R+1][C].setEnabled(true);
							if(R > 0 && m[R-1][C] == 2)				cmdGrid[R-1][C].setEnabled(true);
							if(C < m[0].length-1 && m[R][C+1] == 2)	cmdGrid[R][C+1].setEnabled(true);
							if(C > 0 && m[R][C-1] == 2)		        cmdGrid[R][C-1].setEnabled(true);
						}
						break;

				   case DelPed:
						if(m[i][j]==2){
							setPulsante(cmdGrid[i][j],false);
						}else{
							boolean isEn=((m[i][j]==0 && Startup.dati.turno==GameRound.Player1)||(m[i][j]==1 && !(Startup.dati.turno==GameRound.Player1)))?false:true;
							if(useSymbols())
								setPulsante(cmdGrid[i][j], isEn, Startup.dati.getPlayers()[m[i][j]].getSymbol());
							else
								setPulsante(cmdGrid[i][j], isEn, Startup.dati.getPlayers()[m[i][j]].getIcon());
						}
						break;

					case Win:
						if(m[i][j]==2)
						{
							cmdGrid[i][j].setText("");
							cmdGrid[i][j].setIcon(null);
						}
						cmdGrid[i][j].setEnabled(false);
						break;
				}
	}

	public void setPulsante(JButton p,boolean isEnable)
	{
		p.setEnabled(isEnable);
		p.setIcon(null);
		p.setText("");
		p.setForeground(Color.LIGHT_GRAY);
		p.setBackground(null);
	}

	public void setPulsante(JButton p,boolean isEnable,String S)
	{
		p.setEnabled(isEnable);
		p.setText(S);
		p.setIcon(Startup.imgPulsante);
		p.setForeground(Color.LIGHT_GRAY);
	}

 public void setPulsante(JButton p,boolean isEnable,ImageIcon i)
	{
		p.setEnabled(isEnable);
		p.setIcon(i);
		p.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(!Startup.dati.getPlayers()[Startup.dati.turno.ordinal()].isCPU())
		{
			for(byte i=0; i < cmdGrid.length; i++)
				for(byte j=0; j < cmdGrid[i].length; j++)
					if(cmdGrid[i][j] == ((JButton)e.getSource())) //Controllo quale pulsante è stato premuto
						switch(Startup.core.press(i,j)){         //Invio i dati al core e ricevo esito
							case 0:
								return;

							case -1:
								JOptionPane.showMessageDialog(this, "Mossa non valida!");
								return;

							case -2:
								JOptionPane.showMessageDialog(this, "Tentativo di inserire piu' di due pedine contigue");
								return;

							case -3:
								JOptionPane.showMessageDialog(this, "Nessuna mossa disponibile, passi automaticamente il turno");
								return;
						}
			//Se arriva qui significa che il pulsante non è stato trovato(errore di programmazione)
			JOptionPane.showMessageDialog(this, "errore: Pulsante non trovato!");
		}
	}
	public boolean useSymbols(){ return Startup.useSymbols; }
}