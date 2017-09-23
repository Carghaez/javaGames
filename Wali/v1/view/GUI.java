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

public class GUI
	extends JFrame
	implements ActionListener
{
	//Pannelli principali
	private GamePanel pnlGame;
	private ConfigPanel pnlConfig;
	//Pannello di sfondo
	private JBackgroundPanel pnlMain;

	//Barra dei menu
	private JMenuBar menuBar;
		private JMenu gameMenu, aboutMenu, soundRdMenu;
			//Bottoni del menu
			private JMenuItem optionsItem, newItem, aboutItem;
			private JRadioButtonMenuItem yesItem, noItem;
			private ButtonGroup soundOptGroup;

	public GUI()
	{
		super("Wali: il gioco africano!"); // Titolo della finestra

		//Setup finestra
		this.setLayout(null);
		this.setBounds(/*Left:*/150,/*Top:*/10,/*Width:*/800,/*Height:*/630);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//Creo i controlli
		createControls();

		//Visualizzo la finestra
		setVisible(true);
	}

	public void createControls()
	{

		//Alloco la barra dei menu.
		menuBar = new JMenuBar();

		//Alloco i menu
		gameMenu = new JMenu("Game");
		gameMenu.setMnemonic(KeyEvent.VK_G);
		aboutMenu = new JMenu("?");
		soundRdMenu = new JMenu("Sound");

		//Alloco gli elementi del menu
		newItem = new JMenuItem("New");
		optionsItem = new JMenuItem("Options");
		aboutItem = new JMenuItem("About");
		yesItem = new JRadioButtonMenuItem("Yes");
		noItem = new JRadioButtonMenuItem("No");

		//Setto gli ActionCommand
		newItem.setActionCommand("NEW");
		optionsItem.setActionCommand("OPTIONS");
		aboutItem.setActionCommand("ABOUT");
		yesItem.setActionCommand("y");
		noItem.setActionCommand("n");

		//Radio Gruppo suoni
		soundOptGroup = new ButtonGroup();
		soundOptGroup.add(yesItem);
		soundOptGroup.add(noItem);
		yesItem.setSelected(true);

		//Addo all'ActionListener della GUI
		newItem.addActionListener(this);
		optionsItem.addActionListener(this);
		aboutItem.addActionListener(this);
		yesItem.addActionListener(this);
		noItem.addActionListener(this);

		//Addo gli elementi al menu
		gameMenu.add(newItem);
		gameMenu.addSeparator();
		gameMenu.add(optionsItem);
		gameMenu.add(soundRdMenu);

		aboutMenu.add(aboutItem);

		soundRdMenu.add(yesItem);
		soundRdMenu.add(noItem);

		//Addo i menu alla barra menu
		menuBar.add(gameMenu);
		menuBar.add(aboutMenu);

		//Inserimento della barra nella finestra
		setJMenuBar(menuBar);

		//Alloco i due pannelli
		pnlMain = new JBackgroundPanel();
		pnlConfig = new ConfigPanel();

		getContentPane().add(pnlMain);
		setReady(false);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JRadioButtonMenuItem)
		{
			if(e.getActionCommand().equals("y"))
			{
				Startup.useSounds = true;
				if(optionsItem.isEnabled()) Startup.soundManager.play("main.mp3");
			}else{
				Startup.useSounds = false;
				Startup.soundManager.stopAll();
			}
		}else{
			if (e.getActionCommand().equals("ABOUT"))
			{
				//Visualizzo l'about
				showAboutDialog();
			}

			if (e.getActionCommand().equals("OPTIONS"))
			{
				//Visualizzo le opzioni
				showConfigPanel();
			}

			if (e.getActionCommand().equals("NEW"))
			{
				//Inizio partita
				showGamePanel(Startup.dati);
			}
		}
	}

	public JPanel getMainPanel()
	{
		return pnlMain;
	}

	public JPanel getGamePanel()
	{
		return pnlGame;
	}

	public void setReady(boolean x)
	{
		newItem.setEnabled(x);
	}

	public void showConfigPanel()
	{
		pnlMain.add(pnlConfig);
		optionsItem.setEnabled(false);
		newItem.setEnabled(false);
		this.validate();
		this.repaint();
	}

	public void closeGamePanel()
	{
		setGameBar(true);
		pnlMain.remove(pnlGame);
		pnlConfig.setEnabledPanel(true);
		Startup.core=null;
		pnlGame = null;
		this.validate();
		this.repaint();
	}

	public void setGameBar(boolean x)
	{
		newItem.setEnabled(x);
		optionsItem.setEnabled(x);
	}

	public void showGamePanel(GameData _dati)
	{
		Startup.core = new GameCore(_dati);
		pnlGame = new GamePanel();

		pnlMain.remove(pnlConfig);
		//Inserimento del pannello e visualizzazione della finestra
		pnlMain.add(pnlGame);
		this.validate();
		this.repaint();

		setGameBar(false);

		//Avvio del core
		Startup.core.startGame();
	}

	public void showAboutDialog()
	{
			final AboutDialog aboutDialog = new AboutDialog();
			aboutDialog.setVisible(true);
	}
}

class JBackgroundPanel extends JPanel
{
	private Image sfondo;

	public JBackgroundPanel()
	{
		super();
		setBackground(Color.LIGHT_GRAY);
		setSize(800,600);
		setLocation(0,0);
		setLayout(null);

		//Setting dell'immagine di sfondo
		Toolkit tk= Toolkit.getDefaultToolkit();
		sfondo = tk.getImage(Startup.urlSfondo);
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(sfondo,2);
		try { mt.waitForID(2); }
		catch (InterruptedException e){}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(sfondo,0,0,null);   // Immagine,posizione,posizione,oggetto a cui notificare il caricamento
	}
}