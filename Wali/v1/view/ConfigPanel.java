package javaGames.Wali.v1.view;

import javaGames.Wali.v1.controller.*;
import javaGames.Wali.v1.model.*;
import javaGames.Wali.v1.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConfigPanel
	extends JPanel
	implements ActionListener
{
	private PlayerDataPanel[] pnlPlayer;
	private JLabel lblTitolo;
	private JButton cmdSalva;
	private ButtonGroup rdGroup;
	private JRadioButton rdSymbols;
	private JRadioButton rdIcons;
	private JLabel lblPedine;
	private String radioPress;
	public ConfigPanel()
	{
		super();
		//Setup pannello
		setSize(800,400);
		setLocation(0,0);
		setVisible(true);

		//Sfondo trasparente
		setBackground(null);
		setOpaque(false);

		//Crea i controlli
		createControls();

		//Definisco il layout degli oggetti
		setupLayout();
		radioPress = "s";
	}

	// Crea i controlli
	public void createControls()
	{
		// Creo il titolo
		lblTitolo = new JLabel("Opzioni principali");
		lblTitolo.setFont(new Font("Serif", Font.BOLD, 40));

		//Settaggio JLabel Pedine
		lblPedine = new JLabel("Seleziona tipologia di pedina");

		//Settaggio JButton SALVA
		cmdSalva = new JButton("Salva configurazione");
		cmdSalva.addActionListener(this);

		// Crea i pannelli
		pnlPlayer = new PlayerDataPanel[2];
		pnlPlayer[0] = new PlayerDataPanel("Player1");
		pnlPlayer[1] = new PlayerDataPanel("Player2");

		//Creo i radioButton
		rdSymbols = new JRadioButton("Simboli");
		rdSymbols.setOpaque(false);
		rdSymbols.addActionListener(this);
		rdSymbols.setActionCommand("s");
		rdSymbols.setSelected(true);

		rdIcons = new JRadioButton("Icone");
		rdIcons.setOpaque(false);
		rdIcons.addActionListener(this);
		rdIcons.setActionCommand("i");

		// Crea il ButtonGroup e registra i RadioButton
		ButtonGroup group = new ButtonGroup();
		group.add(rdSymbols);
		group.add(rdIcons);
	}

	// Imposta il layout
	public void setupLayout()
	{
		//Alloco e inizializzo il GroupLayout che utilizzerò nel form
		GroupLayout layout = new GroupLayout(this);
		/*Info sul GroupLayout:
		 * Esso si basa sul concetto di gruppo, può contenere:
		 * Controlli " .addComponent(Controllo,Lunghezza/Larghezza Minima,Preferita,Massima) "
		 * Spazi    "  .addPreferredGap(Lunghezza/Larghezza LayoutStyle.ComponentPlacement.UNRELATED, Default, Max o Short.MAX_VALUE)"
		 * PS I PARALLEL NON HANNO GAP!
		 * Gruppi "    .addGroup( nel caso Orizzontale: layout.createParallelGroup / Verticale: layout.createSequentialGroup() "
		 * Per descrivere l'interfaccia si devono descrivere i gruppi separati in Orizzontale e Verticale
		 *
		 */

		//Setto il layout del contenitore del form a quello appena inizializzato
		this.setLayout(layout);
		//Setto lo spazio tra i controlli in modo automatico qualora non specificato
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		//Vista Orizzontale del LAYOUT
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(lblTitolo)
					.addGroup(layout.createSequentialGroup()
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 30, 30)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							//.addComponent(lblVersus)
							//.addComponent(cmbVersus, 170, 170,170)
							.addComponent(lblPedine)

							.addGroup(layout.createSequentialGroup()
								.addComponent(rdSymbols)
								.addComponent(rdIcons)
							)
							.addComponent(cmdSalva)
						)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 25, 25)
						.addComponent(pnlPlayer[0], 260, 260, 260)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 30, 30)
						.addComponent(pnlPlayer[1], 260, 260, 260)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					)
		);

		//Vista Verticale del LAYOUT
		layout.setVerticalGroup(layout.createSequentialGroup()
					.addComponent(lblTitolo)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 30, 30)
					.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
							//.addComponent(lblVersus, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							//.addComponent(cmbVersus, GroupLayout.PREFERRED_SIZE, 20, 20)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 30, 30)
							.addComponent(lblPedine, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGroup(layout.createParallelGroup()
								.addComponent(rdSymbols, 25, 25, 25)
								.addComponent(rdIcons, 25, 25, 25)
							)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 20, 20)
							.addComponent(cmdSalva, 30, 30, 30)
						)
						.addComponent(pnlPlayer[0], 180, 180, 180)
						.addComponent(pnlPlayer[1], 180, 180, 180)
					)
		);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JButton)
		{
			if (pnlPlayer[0].isComplete() && pnlPlayer[1].isComplete())
			{
				Startup.useSymbols = (radioPress.equals("s"))? true : false;
				if (
						(Startup.useSymbols) ?
							!pnlPlayer[0].getSymbol().equals(pnlPlayer[1].getSymbol()) :
							!pnlPlayer[0].getIcon().equals(pnlPlayer[1].getIcon())
					)
				{
					setEnabledPanel(false);
					boolean isCPU1 = (pnlPlayer[0].chkCPU.getSelectedObjects() != null)? true : false;
					boolean isCPU2 = (pnlPlayer[1].chkCPU.getSelectedObjects() != null)? true : false;
					if(Startup.useSymbols)
						//Uso il costruttore Simboli = stringhe
						Startup.dati = new GameData(
														//Player1
														0, //id
														pnlPlayer[0].txtNome.getText(), //Nick
														((String)pnlPlayer[0].cmbPedina.getSelectedItem()), //Simbolo
														isCPU1, //è una cpu?

														//Player2
														1,
														pnlPlayer[1].txtNome.getText(),
														((String)pnlPlayer[1].cmbPedina.getSelectedItem()),
														isCPU2
													);
					else
						//Uso il costruttore Simboli = icone
						Startup.dati = new GameData(
														//Player1
														0,
														pnlPlayer[0].txtNome.getText(),
														((ImageIcon)pnlPlayer[0].cmbPedina.getSelectedItem()),
														Startup.dati.nomiIcone[pnlPlayer[0].cmbPedina.getSelectedIndex()],
														isCPU1,

														//Player2
														1,
														pnlPlayer[1].txtNome.getText(),
														((ImageIcon)pnlPlayer[1].cmbPedina.getSelectedItem()),
														Startup.dati.nomiIcone[pnlPlayer[1].cmbPedina.getSelectedIndex()],
														isCPU2
													);
					Startup.window.setReady(true);
				}else{
					JOptionPane.showMessageDialog(null, "Scegli due simboli diversi!");
					Startup.useSymbols = !Startup.useSymbols;
				}
			}else{
				JOptionPane.showMessageDialog(null, "Completa tutti i campi!");
			}
		}
		if(e.getSource() instanceof JRadioButton)
		{
				radioPress = e.getActionCommand();
				if(radioPress.equals("s"))
				{
					pnlPlayer[0].addSymbols();
					pnlPlayer[1].addSymbols();
				}else{
					pnlPlayer[0].addIcons();
					pnlPlayer[1].addIcons();
				}
		}
	}

	public void setEnabledPanel(boolean x)
	{
		for (int i = 0; i < pnlPlayer.length; i++)
			pnlPlayer[i].setEnabled(x);
		cmdSalva.setEnabled(x);
		rdIcons.setEnabled(x);
		rdSymbols.setEnabled(x);
	}
}