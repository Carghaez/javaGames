package javaGames.Wali.v0.view;

import javaGames.Wali.v0.controller.*;
import javaGames.Wali.v0.model.*;
import javaGames.Wali.v0.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConfigFrame
	extends JFrame
	implements ActionListener
{
	private PlayerDataFrame[] playerPanels;
	private JLabel lblTitolo;
	private JLabel lblVersus;
	private JComboBox cmbVersus;
	private JButton cmdGioca;

	public ConfigFrame()
	{
		super("Configurazione Iniziale");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.LIGHT_GRAY);

		createControls();
		setupLayout();

		// Show All
		setBounds(150,0,800,300);
		setResizable(false);
		setVisible(true);
	}

	// Crea i controlli
	public void createControls()
	{
		// Creo il titolo
		lblTitolo = new JLabel("WALI: Il gioco africano!");
		lblTitolo.setFont(new Font("Serif", Font.BOLD, 40));

		//Settaggio JLabel VERSUS
		lblVersus = new JLabel("Seleziona tipologia di giocatori");

		//Settaggio ComboBox
		cmbVersus=new JComboBox();
		cmbVersus.setEditable(false);
		cmbVersus.addItem("Player1 vs Player2");
		cmbVersus.addItem("Player1 vs CPU");
		cmbVersus.addActionListener(this);

		//Settaggio JButton GIOCA
		cmdGioca = new JButton("Avvia il gioco");
		cmdGioca.addActionListener(this);

		// Crea i pannelli
		playerPanels = new PlayerDataFrame[2];
		playerPanels[0] = new PlayerDataFrame("Player1");
		playerPanels[1] = new PlayerDataFrame("Player2");
	}

	// Imposta il layout
	public void setupLayout()
	{
		//Alloco e inizializzo il GroupLayout che utilizzerò nel form
		GroupLayout layout = new GroupLayout(this.getContentPane());
		/*Info sul GroupLayout:
		 * Esso si basa sul concetto di gruppo, può contenere:
		 * Controlli " .addComponent(Controllo,Lunghezza/Larghezza Minima,Preferita,Massima) "
		 * Spazi    "  .addPreferredGap(Lunghezza/Larghezza LayoutStyle.ComponentPlacement.UNRELATED, Default, Max o Short.MAX_VALUE)"
		 * Gruppi "    .addGroup( nel caso Orizzontale: layout.createParallelGroup / Verticale: layout.createSequentialGroup() "
		 * Per descrivere l'interfaccia si devono descrivere i gruppi separati in Orizzontale e Verticale
		 *
		 */

		//Setto il layout del contenitore del form a quello appena inizializzato
		getContentPane().setLayout(layout);
		//Setto lo spazio tra i controlli in modo automatico qualora non specificato
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		//Vista Orizzontale del LAYOUT
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(lblTitolo)
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(lblVersus)
							.addComponent(cmbVersus, 170, 170,170)
							.addComponent(cmdGioca)
						)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 30, 30)
						.addComponent(playerPanels[0], 220, 220, 220)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 50, 50)
						.addComponent(playerPanels[1], 220, 220, 220)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		//Vista Verticale del LAYOUT
		layout.setVerticalGroup(layout.createSequentialGroup()
					.addComponent(lblTitolo)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 30, 30)
					.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
							.addComponent(lblVersus, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(cmbVersus, GroupLayout.PREFERRED_SIZE, 20, 20)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 60, 60)
							.addComponent(cmdGioca, GroupLayout.PREFERRED_SIZE, 30, 30)
						)
						.addComponent(playerPanels[0], 135, 135, 135)
						.addComponent(playerPanels[1], 135, 135, 135)));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JComboBox)
			playerPanels[1].setCPU(cmbVersus.getSelectedIndex() == 1);
		if (e.getSource() instanceof JButton)
		{
			if (playerPanels[0].isComplete() && playerPanels[1].isComplete()){
				if (!playerPanels[0].getSymbol().equals(playerPanels[1].getSymbol())){
					setEnabledPanel(false);
					boolean isCPU=(cmbVersus.getSelectedIndex() == 1)?true:false;
					Startup.dati= new GameData(playerPanels[0].txtNome.getText(),((String)playerPanels[0].cmbSimbolo.getSelectedItem()),false,playerPanels[1].txtNome.getText(),((String)playerPanels[1].cmbSimbolo.getSelectedItem()),isCPU);
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() { Startup.creaGameFrame(); }
						});
					}else{
						JOptionPane.showMessageDialog(null, "Scegli due simboli diversi!");
					}
				}else{
					JOptionPane.showMessageDialog(null, "Completa tutti i campi!");
				}
		}
	}

	public void setEnabledPanel(boolean x)
	{
		for (int i = 0; i < playerPanels.length; i++)
				playerPanels[i].setEnabled(x);
		cmbVersus.setEnabled(x);
		cmdGioca.setEnabled(x);
	}
}

class PlayerDataFrame extends JPanel
{
	// Controlli
	private JLabel lblGiocatore;
	private JLabel lblNome;
	public JTextField txtNome;
	public JLabel lblSimbolo;
	public JComboBox cmbSimbolo;
	private String ply;

	public PlayerDataFrame(String ply)
	{
		super();
		this.ply = ply;
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		setBackground(Color.LIGHT_GRAY);

		createControls();
		setupLayout();

		if(ply.equals("Player2"))   cmbSimbolo.setSelectedItem(GameData.simbolo[1]);
		// Show all
		setVisible(true);
	}

	// Aggiunge i simboli ad una combobox
	public static void addSymbols(JComboBox box)
	{
		box.removeAllItems();
		for(int i=0;i<GameData.simbolo.length;i++) box.addItem(GameData.simbolo[i]);
	}

	// Crea i controlli
	public void createControls()
	{
		//Settaggio JLabel Giocatore
		lblGiocatore = new JLabel();
		lblGiocatore.setText(ply);
		lblGiocatore.setFont(new Font("Serif", Font.BOLD, 25));
		lblGiocatore.setOpaque(false);

		//Settaggio JLabel Nome
		lblNome=new JLabel();
		lblNome.setText("Inserisci il nome");
		lblNome.setOpaque(false);

		//Settaggio TextBox Nome
		txtNome = new JTextField();
		txtNome.setText(ply);

		//Settaggio JLabel Simbolo
		lblSimbolo=new JLabel();
		lblSimbolo.setText("Scegli il simbolo");
		lblSimbolo.setOpaque(false);

		// Settaggio ComboBox
		cmbSimbolo = new JComboBox();
		cmbSimbolo.setEditable(false);
		addSymbols(cmbSimbolo);
	}

	// Imposta il layout
	public void setupLayout()
	{
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addComponent(lblGiocatore)
			.addGroup(layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
						.addComponent(lblNome)
						.addComponent(txtNome)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
					 )
					.addGroup(layout.createSequentialGroup()
						.addComponent(lblSimbolo)
						.addComponent(cmbSimbolo)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
					 )
			)
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addComponent(lblGiocatore, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 15, 15)
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(lblNome, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				 )
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(lblSimbolo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmbSimbolo, GroupLayout.PREFERRED_SIZE, 20, 20)
				)
			)
			.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 20, 20)
		);
	}

	@Override
	public void setEnabled(boolean x)
	{
		txtNome.setEnabled(x);
		cmbSimbolo.setEnabled(x);
	}

	public void setCPU(boolean isCPU)
	{
		txtNome.setEnabled(!isCPU);
		cmbSimbolo.setEnabled(!isCPU);
		cmbSimbolo.removeAllItems();

		if (isCPU)
		{
			lblGiocatore.setText("CPU");
			txtNome.setText("CPU");
			cmbSimbolo.addItem("C");
		}else{
			lblGiocatore.setText(ply);
			txtNome.setText(ply);
			addSymbols(cmbSimbolo);
		}
	}

	public String getSymbol()
	{
		return (String)cmbSimbolo.getSelectedItem();
	}

	public boolean isComplete()
	{
		return !txtNome.getText().equals("");
	}
}