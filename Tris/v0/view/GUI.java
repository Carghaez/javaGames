package javaGames.Tris.v0.view;

import javaGames.Tris.v0.controller.*;
import javaGames.Tris.v0.model.*;
import javaGames.Tris.v0.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GUI
	extends JFrame
{

	public GUI()
	{
		super();
		initComponents();
	}

	public void initComponents()
	{
		//Settaggio GUI.
		this.setBounds(10,10,800,600);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}