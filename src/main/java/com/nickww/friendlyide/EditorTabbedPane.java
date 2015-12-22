package com.nickww.friendlyide;

import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class EditorTabbedPane
{
	private final JTabbedPane tabbedPane;
	
	public EditorTabbedPane(KeyListener keyListener)
	{
		tabbedPane = new JTabbedPane();
		tabbedPane.addKeyListener(keyListener);
		tabbedPane.setFocusTraversalKeysEnabled(false);
		tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "none");
		tabbedPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "none");
	}
	
	public JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}
	
	public void tabLeft()
	{
		int newIndex = tabbedPane.getSelectedIndex() - 1;
		if(newIndex < 0)
			newIndex = tabbedPane.getTabCount() - 1;
		tabbedPane.setSelectedIndex(newIndex);
	}
	
	public void tabRight()
	{
		int newIndex = tabbedPane.getSelectedIndex() + 1;
		if(newIndex >= tabbedPane.getTabCount())
			newIndex = 0;
		tabbedPane.setSelectedIndex(newIndex);
	}
	
	public Editor getVisibleEditor()
	{
		return (Editor)tabbedPane.getSelectedComponent();
	}
	
	public void open()
	{
		try
		{
			JFileChooser chooser = new JFileChooser(".");
			int result = chooser.showOpenDialog(tabbedPane);
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				tabbedPane.addTab(file.getName(), new Editor(file));
			}
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(tabbedPane, "Could not read the selected file.", "File not found", JOptionPane.ERROR_MESSAGE);
		}
	}
}
