package javaGames.Wali.v1.view;

import javaGames.Wali.v1.controller.*;
import javaGames.Wali.v1.model.*;
import javaGames.Wali.v1.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlayerDataPanel
	extends JPanel
{
	// Controlli
	private JLabel lblGiocatore;
	private JLabel lblNome;
	public JTextField txtNome;
	public JLabel lblSimbolo;
	public JComboBox cmbPedina;
	public JCheckBox chkCPU;
	private String playerName;
	private PiecesRenderer render;

	public PlayerDataPanel(String player)
	{
		super();
		playerName = player;
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		setBackground(null);
		setOpaque(false);

		createControls();
		setupLayout();

		if(playerName.equals("Player2"))   cmbPedina.setSelectedItem(GameData.simbolo[1]);
		// Show all
		setVisible(true);
	}

	// Crea i controlli
	public void createControls()
	{
		//Settaggio JLabel Giocatore
		lblGiocatore = new JLabel();
		lblGiocatore.setText(playerName);
		lblGiocatore.setFont(new Font("Serif", Font.BOLD, 25));
		lblGiocatore.setOpaque(false);

		//Settaggio JLabel Nome
		lblNome=new JLabel();
		lblNome.setText("Inserisci il nome");
		lblNome.setOpaque(false);

		//Settaggio TextBox Nome
		txtNome = new JTextField();
		txtNome.setText(playerName);

		//Settaggio JLabel Simbolo
		lblSimbolo=new JLabel();
		lblSimbolo.setText("Scegli il simbolo");
		lblSimbolo.setOpaque(false);

		// Settaggio ComboBox
		cmbPedina = new JComboBox(GameData.simbolo);
		cmbPedina.setEditable(false);
		cmbPedina.setMaximumRowCount(10);
		render = new PiecesRenderer();
		cmbPedina.setRenderer(render);

		//Settaggio CheckBox
		chkCPU = new JCheckBox("CPU");
		chkCPU.setMnemonic(KeyEvent.VK_C);
		chkCPU.setOpaque(false);

	}

	// Imposta il layout
	public void setupLayout()
	{
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
			.addGroup(layout.createParallelGroup()
					.addGroup(layout.createSequentialGroup()
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblGiocatore)
						.addComponent(chkCPU)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

					)
					.addGroup(layout.createSequentialGroup()
						.addComponent(lblNome)
						.addComponent(txtNome)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
					 )
					.addGroup(layout.createSequentialGroup()
						.addComponent(lblSimbolo)
						.addComponent(cmbPedina)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
					 )
			)
	   );

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(lblGiocatore, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(chkCPU)
				)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(lblNome, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				 )
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(lblSimbolo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmbPedina, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				)
			)
			.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 20, 20)
	   );
	}

	@Override
	public void setEnabled(boolean x)
	{
		txtNome.setEnabled(x);
		cmbPedina.setEnabled(x);
		chkCPU.setEnabled(x);
	}

	public String getSymbol()
	{
		return (String)cmbPedina.getSelectedItem();
	}

	public ImageIcon getIcon()
	{
		return (ImageIcon)cmbPedina.getSelectedItem();
	}

	public boolean isComplete()
	{
		return !txtNome.getText().equals("");
	}

	// Aggiunge i simboli ad una combobox
	public void addSymbols()
	{
		cmbPedina.removeAllItems();
		//cmbPedina.removeAll();
		for(int i=0; i < GameData.simbolo.length; i++) cmbPedina.addItem(GameData.simbolo[i]);
		cmbPedina.setMaximumRowCount(10);
	}
	public void addIcons()
	{
		cmbPedina.removeAllItems();
		//cmbPedina.removeAll();
		for(int i=0; i < GameData.icona.length; i++) cmbPedina.addItem(GameData.icona[i]);
		cmbPedina.setMaximumRowCount(3);
	}
}

class PiecesRenderer
	extends JLabel
	implements ListCellRenderer
{
	 public PiecesRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	 }

	 public Component getListCellRendererComponent(
		 JList list,
		 Object value,
		 int index,
		 boolean isSelected,
		 boolean cellHasFocus)
	 {
		if(value instanceof String)
		{
			setText(value.toString());
			setIcon(null);
		}else{
			setIcon((ImageIcon)value);
			setText("");
		}
		if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
		} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
		}
		 //setBackground(isSelected ? Color.green : Color.white);
		 //setForeground(isSelected ? Color.white : Color.black);
		 return this;
	 }
 }