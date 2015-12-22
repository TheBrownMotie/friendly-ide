package com.nickww.friendlyide;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
		ide.addKeyListener(ide);
		ide.setFocusTraversalKeysEnabled(false);
		ide.setVisible(true);
	}
	
	private List<Editor> editors;
	private int visibleEditor;
	private KeyboardMap keyboardMap;
	
	public IDE()
	{
		this.editors = new ArrayList<>();
		keyboardMap = new Dvorak();
		addEditor();
	}
	
	public void addEditor()
	{
		Editor e = new Editor();
		this.editors.add(e);
		this.visibleEditor = this.editors.size() - 1;
		this.add(e);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		Editor editor = editors.get(visibleEditor);
		keyboardMap.handle(editor, e);
		editor.repaint();
	}
	
	@Override 
	public void keyReleased(KeyEvent e) {}
	@Override 
	public void keyTyped(KeyEvent e) {}
}
