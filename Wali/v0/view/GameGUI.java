package javaGames.Wali.v0.view;

import javaGames.Wali.v0.controller.*;
import javaGames.Wali.v0.*;
import javaGames.Wali.v0.model.*;
import javaGames.Wali.v0.model.GameData.GameRound;
import javaGames.Wali.v0.model.GameData.GameState;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GameGUI
	extends JFrame
{
	private GamePanel pnlGame;

	public GameGUI(GameData _dati)
	{
		super("Wali"); // Titolo della finestra
		Startup.core=new GameCore(_dati);
		pnlGame= new GamePanel();

		//Setup finestra
		setLayout(null);
		setBounds(150,100,800,600);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		//Inserimento del pannello e visualizzazione della finestra
		Container c= getContentPane();
		c.add(pnlGame);
		setVisible(true);

		//Avvio del core
		Startup.core.start();
	}
}

class GamePanel extends JPanel
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

class ContainerPanel extends JPanel
{
	private BackPanel pnlBack;
	private InfoPanel pnlInfo;

	public ContainerPanel()
	{
		super();
		//Setup JPanel Contenitore
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);

		//Setup JPanel Background (conterrà lo sfondo)
		pnlBack=new BackPanel();

		//Setup JPanel Info
		pnlInfo = new InfoPanel();

		add(pnlBack);
		add(pnlInfo);
	}
	public InfoPanel getpnlInfo(){ return pnlInfo; }
}

class BackPanel extends JPanel implements Observer
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
		setBackground(Color.LIGHT_GRAY);

		//Setting dell'immagine di sfondo
		Toolkit tk= Toolkit.getDefaultToolkit();
		Sfondo = tk.getImage(Startup.urlSfondo);
		MediaTracker mt=new MediaTracker(this);
		mt.addImage(Sfondo,1);
		try { mt.waitForID(1); }
		catch (InterruptedException e){}

		//Setup JPanel griglia
		pnlGrid=new GridPanel();
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

class GridPanel extends JPanel implements ActionListener
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
				cmdGrid[i][j].setBackground(null);
				cmdGrid[i][j].setOpaque(true);
				cmdGrid[i][j].setContentAreaFilled(false);
				cmdGrid[i][j].setFont(new Font("Serif", Font.BOLD, 40)); //Setup del font
				cmdGrid[i][j].setHorizontalTextPosition(0); //Setta lo sfondo e il testo su due layer diversi
				cmdGrid[i][j].addActionListener(this);
				cmdGrid[i][j].setForeground(Color.LIGHT_GRAY);
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
							setPulsante(cmdGrid[i][j],true,Startup.dati.getPlayers()[m[i][j]].getSymbol());
						break;

					case Select:
						if(m[i][j]==2){
							setPulsante(cmdGrid[i][j],false);
						}else{
							boolean isEn=((m[i][j]==0 && Startup.dati.turno==GameRound.Player1)||(m[i][j]==1 && !(Startup.dati.turno==GameRound.Player1)))?true:false;
							setPulsante(cmdGrid[i][j],isEn,Startup.dati.getPlayers()[m[i][j]].getSymbol());
						}
						break;

					case Move:
						byte R=Startup.dati.getScacchiera().getRigSel();
						byte C=Startup.dati.getScacchiera().getColSel();
						if(i==R && j==C)
						{
							cmdGrid[i][j].setForeground(Color.YELLOW);
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
							setPulsante(cmdGrid[i][j],isEn,Startup.dati.getPlayers()[m[i][j]].getSymbol());
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
	}
	public void setPulsante(JButton p,boolean isEnable,String S)
	{
		p.setEnabled(isEnable);
		p.setText(S);
		p.setIcon(Startup.imgPulsante);
		p.setForeground(Color.LIGHT_GRAY);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		for(byte i=0;i<cmdGrid.length;i++)
			for(byte j=0;j<cmdGrid[i].length;j++)
				if(cmdGrid[i][j]==((JButton)e.getSource())) //Controllo quale pulsante è stato premuto
					switch(Startup.core.press(i,j)){         //Invio i dati al core e ricevo esito
						case 0:
							return;

						case -1:
							JOptionPane.showMessageDialog(null, "Mossa non valida!");
							return;

						case -2:
							JOptionPane.showMessageDialog(null, "Tentativo di inserire piu' di due pedine contigue");
							return;

						case -3:
							JOptionPane.showMessageDialog(null, "Nessuna mossa disponibile, passi automaticamente il turno");
							return;
					}
		//Se arriva qui significa che il pulsante non è stato trovato(errore di programmazione)
		JOptionPane.showMessageDialog(null, "Pulsante non trovato!");
	}
}
