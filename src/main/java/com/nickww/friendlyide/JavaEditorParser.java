package com.nickww.friendlyide;

import javafx.scene.paint.Color;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaEditorParser extends VoidVisitorAdapter<Object>
{
	private static final int spacesPerTab = 8;
	
	private final Editor editor;
	
	public JavaEditorParser(Editor e)
	{
		this.editor = e;
	}
	
	private java.awt.Color toColor(Color c)
	{
		return new java.awt.Color((int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255));
	}
	
	private CursorRange getRange(Node node)
	{
		int numTabsOnBeginLine = (int)editor.lines().get(node.getBeginLine()).characters().stream().filter(c -> c.getChar() == '\t').count();
		int numTabsOnEndLine = (int)editor.lines().get(node.getEndLine()).characters().stream().filter(c -> c.getChar() == '\t').count();
		int tabAwareBeginColumn = node.getBeginColumn() - (numTabsOnBeginLine * spacesPerTab) + numTabsOnBeginLine - 1;
		int tabAwareEndColumn = node.getEndColumn() - (numTabsOnEndLine * spacesPerTab) + numTabsOnEndLine;
		
		return new CursorRange(new Cursor(node.getBeginLine() - 1, tabAwareBeginColumn), new Cursor(node.getEndLine() - 1, tabAwareEndColumn));
	}
}
