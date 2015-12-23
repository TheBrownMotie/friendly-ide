package com.nickww.friendlyide.keyboardmap;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import com.nickww.friendlyide.Editor;
import com.nickww.friendlyide.EditorTabbedPane;

public class Dvorak implements KeyboardMap
{
	@Override
	public void handle(EditorTabbedPane editorsPane, KeyEvent event)
	{
		Editor editor = editorsPane.getVisibleEditor();
		int keyCode = event.getKeyCode();
		boolean control = (event.getModifiers() & KeyEvent.CTRL_MASK) != 0;
		
		if(event.getKeyCode() == KeyEvent.VK_ALT)
		{
			editor.mark();
			event.consume();
		}
		else if(control && keyCode == KeyEvent.VK_K)
			editor.paste();
		else if(control && keyCode == KeyEvent.VK_Q)
			editor.cut();
		else if((control && keyCode == KeyEvent.VK_COMMA) || keyCode == KeyEvent.VK_UP)
			editor.up(1);
		else if((control && keyCode == KeyEvent.VK_O) || event.getKeyCode() == KeyEvent.VK_DOWN)
			editor.down(1);
		else if((control && keyCode == KeyEvent.VK_A) || keyCode == KeyEvent.VK_LEFT)
			editor.left();
		else if((control && keyCode == KeyEvent.VK_E) || keyCode == KeyEvent.VK_RIGHT)
			editor.right();
		else if(control && keyCode == KeyEvent.VK_N)
			editor.rightToken();
		else if(control && keyCode == KeyEvent.VK_H)
			editor.leftToken();
		else if(control && keyCode == KeyEvent.VK_C)
			editor.up(4);
		else if(control && keyCode == KeyEvent.VK_T)
			editor.down(4);
		else if(keyCode == KeyEvent.VK_HOME)
			editor.home();
		else if(keyCode == KeyEvent.VK_END)
			editor.end();
		else if(keyCode == KeyEvent.VK_DELETE)
			editor.delete();
		else if(keyCode == KeyEvent.VK_BACK_SPACE)
			editor.backspace();
		else if(keyCode == KeyEvent.VK_ENTER)
			editor.enter(true);
		else if(control && keyCode == KeyEvent.VK_R)
			editorsPane.open();
		else if(control && keyCode == KeyEvent.VK_QUOTE)
			editorsPane.tabLeft();
		else if(control && keyCode == KeyEvent.VK_PERIOD)
			editorsPane.tabRight();
		else if(editor.isTypableCharacter((char)keyCode) && (event.getModifiers() == 0 || event.getModifiers() == InputEvent.SHIFT_MASK))
			editor.type(event.getKeyChar());
	}
}
