package javaGames.Halma.v0.view;

import javaGames.Halma.v0.controller.*;
import javaGames.Halma.v0.model.*;
import javaGames.Halma.v0.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public final class BoardPanel
	extends JPanel
	implements ActionListener
{
	public JButton [][] board;

	public BoardPanel()
	{
		super();
		this.setLocation(20, 30);
		this.setSize(500, 450);
		setLayout(new GridLayout(10,10));
		board = new JButton[10][10];
		for(int i = 0 ; i < board.length; i++){
			for(int j = 0 ; j < board[i].length; j++){
				board[i][j] = new JButton();
				initCasella(board[i][j],i,j);
			}
		}
	}

	public void initCasella(JButton casella,int i,int j)
	{
		casella.setVisible(true);
		casella.setForeground(Color.red);
		casella.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		casella.setFont(new Font("Arial",Font.BOLD,22));
		if((i+j)%2 ==0)
			casella.setBackground(Color.BLACK);
		else
			casella.setBackground(Color.white);
		this.add(casella);
		if(Startup.dati.scacchiera[i][j] != 2){
			casella.setBorder(BorderFactory.createLineBorder(Color.RED));
			casella.setText(Startup.dati.Player[Startup.dati.scacchiera[i][j]].simbolo);
		}
		else
			casella.setText("");
		casella.addActionListener(this);
		//casella.setText(""+(i-j));
	}

	public void setCasella(JButton casella,int i,int j)
	{
		if((i+j)%2 ==0)
			casella.setBackground(Color.BLACK);
		else
			casella.setBackground(Color.white);
		if(Startup.dati.scacchiera[i][j]!= 2)
			casella.setText(Startup.dati.Player[Startup.dati.scacchiera[i][j]].simbolo);
		else
			casella.setText("");
		if( Startup.core.getPedSel()!=null &&  (Startup.core.getPedSel().x == j && Startup.core.getPedSel().y == i) )
							board[i][j].setBackground(Color.orange);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( ! (e.getSource() instanceof JButton) )
			return;
		for(byte i = 0 ; i < board.length ; i++ )
			for(byte j = 0 ; j < board[i].length ; j++ )
				if( ( (JButton)e.getSource() ) == board[i][j]){
						Startup.core.press(i,j);
				}
	}

	void refresh()
	{
		for(int i = 0 ; i < board.length; i++){
			for(int j = 0 ; j < board[i].length; j++){
				setCasella(board[i][j],i,j);
			}
		}
	}
}