package com.nickww.friendlyide;

import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class EditorTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;
	
	public EditorTabbedPane(KeyListener keyListener)
	{
		super();
		super.addKeyListener(keyListener);
		super.setFocusTraversalKeysEnabled(false);
		super.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "none");
		super.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "none");
	}
	
	public void tabLeft()
	{
		int newIndex = super.getSelectedIndex() - 1;
		if(newIndex < 0)
			newIndex = super.getTabCount() - 1;
		super.setSelectedIndex(newIndex);
	}
	
	public void tabRight()
	{
		int newIndex = super.getSelectedIndex() + 1;
		if(newIndex >= super.getTabCount())
			newIndex = 0;
		super.setSelectedIndex(newIndex);
	}
	
	public Editor getVisibleEditor()
	{
		return (Editor)super.getSelectedComponent();
	}
	
	public void open()
	{
		try
		{
			JFileChooser chooser = new JFileChooser(".");
			int result = chooser.showOpenDialog(this);
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				super.addTab(file.getName(), new Editor(file));
			}
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(this, "Could not read the selected file.", "File not found", JOptionPane.ERROR_MESSAGE);
		}
	}
}
