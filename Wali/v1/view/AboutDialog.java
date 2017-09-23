package javaGames.Wali.v1.view;

import javaGames.Wali.v1.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

// Finestra di about
public class AboutDialog
	extends JDialog
	implements ActionListener
{
	private JLabel image;
	private JLabel text;
	private JButton closeButton;

	public AboutDialog()
	{
		super();

		setTitle("About");
		setModal(true);

		createControls();
		setupLayout();

		pack();
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("CLOSE"))
			setVisible(false);
	}

	private void createControls()
	{
		// Creo i bottoni
		closeButton = new JButton("Close");
		closeButton.setActionCommand("CLOSE");
		closeButton.addActionListener(this);

		image = new JLabel(new ImageIcon(Startup.urlAbout));
		text = new JLabel("<html>Wali<br /><b>By Carpinato Gaetano</b></html>");

		getRootPane().setDefaultButton(closeButton);
	}

	private void setupLayout()
	{
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addGroup(layout.createSequentialGroup()
									.addComponent(image)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 10, 10)
									.addComponent(text)
									)
							.addComponent(closeButton)
						);

		layout.setVerticalGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(image)
									.addComponent(text)
									)
							.addComponent(closeButton)
						);
	}
}