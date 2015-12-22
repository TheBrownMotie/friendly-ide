package com.nickww.friendlyide;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

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
	
	private JTabbedPane tabbedPane;
	private KeyboardMap keyboardMap;
	
	public IDE()
	{
		tabbedPane = new JTabbedPane();
		tabbedPane.setFocusTraversalKeysEnabled(false);
		tabbedPane.addKeyListener(this);
		keyboardMap = new Dvorak();
		addEditor();
		addEditor();
		this.add(tabbedPane);
	}
	
	public void addEditor()
	{
		this.tabbedPane.addTab("", new Editor());
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		Editor editor = (Editor)tabbedPane.getSelectedComponent();
		keyboardMap.handle(editor, e);
		editor.repaint();
	}
	
	@Override 
	public void keyReleased(KeyEvent e) {}
	@Override 
	public void keyTyped(KeyEvent e) {}
}
