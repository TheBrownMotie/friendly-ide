package com.nickww.friendlyide;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFrame;

import com.nickww.friendlyide.keyboardmap.Dvorak;
import com.nickww.friendlyide.keyboardmap.KeyboardMap;

public class IDE extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 1L;

	public static void main(String... args) throws FileNotFoundException
	{
		Configuration.load(new File("src/main/resources/keyword_colormap.conf"));
		IDE ide = new IDE();
		ide.setSize(600, 600);
		ide.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ide.setFocusTraversalKeysEnabled(false);
		ide.setVisible(true);
	}
	
	private EditorTabbedPane tabbedPane;
	private KeyboardMap keyboardMap;
	
	public IDE()
	{
		tabbedPane = new EditorTabbedPane(this);
		keyboardMap = new Dvorak();
		addEditor();
		this.add(tabbedPane.getTabbedPane());
	}
	
	public void addEditor()
	{
		this.tabbedPane.getTabbedPane().addTab("", new Editor());
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		keyboardMap.handle(tabbedPane, e);
		tabbedPane.getVisibleEditor().repaint();
	}
	
	@Override 
	public void keyReleased(KeyEvent e) {}
	@Override 
	public void keyTyped(KeyEvent e) {}
}
