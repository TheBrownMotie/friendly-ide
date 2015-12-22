package com.nickww.friendlyide.keyboardmap;

import java.awt.event.KeyEvent;

import com.nickww.friendlyide.Editor;

public interface KeyboardMap
{
	public void handle(Editor editor, KeyEvent event);
}
