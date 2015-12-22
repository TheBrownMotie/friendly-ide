package com.nickww.friendlyide.keyboardmap;

import java.awt.event.KeyEvent;

import com.nickww.friendlyide.EditorTabbedPane;

public interface KeyboardMap
{
	public void handle(EditorTabbedPane editorsPane, KeyEvent event);
}
