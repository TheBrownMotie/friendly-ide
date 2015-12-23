package com.nickww.friendlyide;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

public class Editor extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	private ResettableTimer saverTimer;
	private File file;
	private List<Line> lines;
	private Cursor cursor;
	private Cursor highlightStart;
	
	public Editor()
	{
		lines = new ArrayList<>();
		lines.add(new Line());
		cursor = new Cursor();
		highlightStart = null;
		file = null;
		saverTimer = new ResettableTimer(2, TimeUnit.SECONDS, this::save);
	}
	
	public Editor(File file) throws FileNotFoundException
	{
		lines = new ArrayList<>();
		lines.add(new Line());
		cursor = new Cursor();
		highlightStart = null;
		saverTimer = new ResettableTimer(2, TimeUnit.SECONDS, this::save);
		
		try(Scanner scanner = new Scanner(file))
		{
			while(scanner.hasNextLine())
				type(scanner.nextLine().toCharArray()).enter(false);
		}
		this.file = file;
	}
	
	public List<Line> lines()
	{
		return lines;
	}
	
	public void save()
	{
		if(file == null)
		{
			JFileChooser chooser = new JFileChooser(".");
			int result = chooser.showSaveDialog(this);
			if(result != JFileChooser.APPROVE_OPTION)
				return;
			file = chooser.getSelectedFile();
		}
		
		try
		{
			Files.write(file.toPath(), toString().getBytes("UTF8"));
			new JavaEditorParser(this).visit(JavaParser.parse(file), null);
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(this, "Could not save to target location", "IO exception", JOptionPane.ERROR_MESSAGE);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		this.repaint();
	}
	
	void color(CursorRange range, Color c)
	{
		for(Line line : getText(range))
			for(Character ch : line.characters())
				ch.setFontColor(c);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		System.out.println("here");
		g2.setColor(Configuration.background);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		final int lineHeight = getLineHeight(g2) + 1;
		final int maxDescent = Character.getMaxDescent(g2, Configuration.fontSize) - 1;
		
		int y = lineHeight;
		int row = 0;
		for (Line line : lines)
		{
			int x = 0;
			int col = 0;
			for(Character character : line.characters())
			{
				boolean highlight = Cursor.isBetween(highlightStart, cursor, new Cursor(row, col));
				int charWidth = character.getTotalWidth(g2, Configuration.fontSize);
				drawBackground(g2, x, y + maxDescent, charWidth, lineHeight, highlight ? Color.BLUE : Color.WHITE);
				character.paint(g2, Configuration.fontSize, x, y);
				drawCursors(g2, x, y - lineHeight + maxDescent, lineHeight, new Cursor(row, col), cursor, highlightStart);
				col++;
				x += charWidth;
			}
			
			drawCursors(g2, x, y - lineHeight + maxDescent, lineHeight, new Cursor(row, col), cursor, highlightStart);
			y += lineHeight;
			row++;
		}
	}
	
	private Editor deleteText(CursorRange range)
	{
		highlightStart = range.getFirst();
		cursor = range.getSecond();
		
		while(!cursor.equals(highlightStart))
		{
			cursor.left(cols(previousRow()));
			if (cols() == cursor.getCol() && currentRow() < lines.size() - 1)
				lines.set(currentRow(), new Line(currentLine(), lines.remove(nextRow())));
			else if(!isAtEnd())
				currentLine().remove(cursor.getCol());
		}
		return this;
	}
	
	private void drawBackground(Graphics2D g, int x, int y, int w, int h, Color c)
	{
		g.setColor(lighten(lighten(c)));
		g.fillRect(x, y - h, w, h);
		g.setColor(c);
		g.drawLine(x, y - h, x + w, y - h);
		g.drawLine(x, y, x + w, y);
	}
	
	private Color lighten(Color c)
	{
		return new Color(
				Math.min(255, c.getRed() + 50),
				Math.min(255, c.getGreen() + 50),
				Math.min(255, c.getBlue() + 50));
	}
	
	private void drawCursors(Graphics2D g, final int x, final int y, final int lineHeight, Cursor position, Cursor... cursors)
	{
		for(Cursor c : cursors)
		{
			if(c != null && c.equals(position))
			{
				g.setColor(Color.BLACK);
				g.drawLine(x, y, x, y + lineHeight);
			}
		}
	}
	
	private Editor copy()
	{
		StringBuilder text = new StringBuilder();
		CursorRange range = new CursorRange(cursor, highlightStart);
		if(range.getFirst().getRow() == range.getSecond().getRow())
		{
			text.append(this.lines.get(range.getFirst().getRow()).subLine(range.getFirst().getCol(), range.getSecond().getCol()));
		}
		else
		{
			text.append(this.lines.get(range.getFirst().getRow()).subLine(range.getFirst().getCol())).append("\n");
			for(int r = range.getFirst().getRow()+1; r < range.getSecond().getRow(); r++)
				text.append(this.lines.get(r)).append("\n");
			text.append(this.lines.get(range.getSecond().getRow()).subLine(0, range.getSecond().getCol()));
		}
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text.toString()), null);
		return this;
	}
	
	private List<Line> getText(CursorRange range)
	{
		List<Line> lines = new ArrayList<>();
		if(range.getFirst().getRow() == range.getSecond().getRow())
		{
			lines.add(this.lines.get(range.getFirst().getRow()).subLine(range.getFirst().getCol(), range.getSecond().getCol()));
		}
		else
		{
			lines.add(this.lines.get(range.getFirst().getRow()).subLine(range.getFirst().getCol()));
			for(int r = range.getFirst().getRow()+1; r < range.getSecond().getRow(); r++)
				lines.add(this.lines.get(r));
			lines.add(this.lines.get(range.getSecond().getRow()).subLine(0, range.getSecond().getCol()));
		}
		return lines;
	}
	
	public Editor mark()
	{
		if(highlightStart == null)
		{
			highlightStart = new Cursor(cursor);
		}
		else
		{
			copy();
			highlightStart = null;
		}
		return this;
	}
	
	public Editor paste()
	{
		try
		{
			String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			for(char c : data.toCharArray())
			{
				if(c == '\n')
					enter(true);
				else
					type(c);
			}
		}
		catch (HeadlessException | UnsupportedFlavorException | IOException e)
		{
			e.printStackTrace();
		}
		return this;
	}
	
	public Editor cut()
	{
		if(highlightStart != null)
			copy().delete();
		return this;
	}
	
	public boolean isTypableCharacter(char character)
	{
		if(character == '\t')
			return true;
	    java.lang.Character.UnicodeBlock block = java.lang.Character.UnicodeBlock.of(character);
	    return (!java.lang.Character.isISOControl(character)) &&
	    		character != KeyEvent.CHAR_UNDEFINED &&
	    		block != null &&
	    		block != java.lang.Character.UnicodeBlock.SPECIALS;
	}
	
	public Editor type(char... c)
	{
		saverTimer.reset();
		for(char ch : c)
		{
			currentLine().type(ch, cursor.getCol());
			right();
			textUpdated();
		}
		return this;
	}
	
	public Editor enter(boolean bracesAware)
	{
		Line newLine = currentLine().splitAt(cursor.getCol());
		lines.add(cursor.getRow() + 1, newLine);
		down(1).home();
		if(bracesAware)
		{
			int numOpenBraces = 0;
			for(Line line : lines)
				for(Character character : line.characters())
					numOpenBraces += (character.getChar() == '{' ? 1 : (character.getChar() == '}' ? -1 : 0));
			for(int count = 0; count < numOpenBraces; count++)
				this.type('\t');
		}
		textUpdated();
		return this;
	}
	
	public Editor rightToken()
	{
		Character.TokenType tokenType = currentCharacter().getTokenType();
		if(isAtEnd())
			return this;
		
		if(cursor.getCol() == cols())
			right();
		while(cursor.getCol() < cols() && currentCharacter().getTokenType() == tokenType)
			right();
		return this;
	}
	
	public Editor leftToken()
	{
		Character.TokenType tokenType = previousCharacter().getTokenType();
		if(isAtBeginning())
			return this;
		
		if(cursor.getCol() == 0)
			left();
		while(cursor.getCol() > 0 && previousCharacter().getTokenType() == tokenType)
			left();
		return this;
	}
	
	public Editor left()
	{
		cursor.left(cols(previousRow()));
		return this;
	}
	
	public Editor right()
	{
		cursor.right(rows(), cols());
		return this;
	}
	
	public Editor up(int numUp)
	{
		for(int i = 0; i < numUp; i++)
			cursor.up(cols(previousRow()));
		return this;
	}
	
	public Editor down(int numDown)
	{
		for(int i = 0; i < numDown; i++)
			cursor.down(rows(), cols(nextRow()));
		return this;
	}
	
	public Editor home()
	{
		cursor.home();
		return this;
	}
	
	public Editor end()
	{
		cursor.end(cols());
		return this;
	}
	
	public Editor backspace()
	{
		if(highlightStart == null)
			cursor.left(cols(previousRow()));
		delete();
		return this;
	}
	
	public Editor delete()
	{
		if(highlightStart != null)
		{
			deleteText(new CursorRange(cursor, highlightStart));
			highlightStart = null;
		}
		else
		{
			if (cols() == cursor.getCol() && currentRow() < lines.size() - 1)
				lines.set(currentRow(), new Line(currentLine(), lines.remove(nextRow())));
			else if(!isAtEnd())
				currentLine().remove(cursor.getCol());
		}
		textUpdated();
		return this;
	}
	
	public Cursor getTextCursor()
	{
		return cursor;
	}
	
	private void textUpdated()
	{
		Line line = currentLine();
		saverTimer.reset();
		for(Line l : lines)
			for(Character c : l.characters())
				c.setFontColor(Color.BLACK);
		
		for(Map.Entry<String, Color> entry : Configuration.keywordColors.entrySet())
			for(int index : line.indicesOf(entry.getKey()))
				for(int j = index; j < index + entry.getKey().length(); j++)
					line.characters().get(j).setFontColor(entry.getValue());
		
		boolean inQuote = false;
		for(Line l : lines)
		{
			for(Character c : l.characters())
			{
				if(c.getChar() == '"')
				{
					inQuote = !inQuote;
					c.setFontColor(Color.GREEN.darker().darker());
				}
				else if(inQuote)
				{
					c.setFontColor(Color.GREEN);
				}
			}
		}
	}
	
	private boolean isAtBeginning()
	{
		return cursor.getRow() == 0 && cursor.getCol() == 0;
	}
	
	private boolean isAtEnd()
	{
		return cursor.getRow() >= rows() - 1 && cursor.getCol() >= cols();
	}
	
	private int rows()
	{
		return lines.size();
	}
	
	private int cols(int row)
	{
		return row < 0 || row >= rows() ? 0 : lines.get(row).size();
	}
	
	private int currentRow()
	{
		return cursor.getRow();
	}
	
	private int previousRow()
	{
		return cursor.getRow() - 1;
	}
	
	private int nextRow()
	{
		return cursor.getRow() + 1;
	}
	
	private Line currentLine()
	{
		return lines.get(currentRow());
	}
	
	private int cols()
	{
		return lines.get(cursor.getRow()).size();
	}
	
	private Character currentCharacter()
	{
		if(cursor.getCol() >= cols())
			return Character.NEWLINE;
		return lines.get(cursor.getRow()).get(cursor.getCol());
	}
	
	private Character previousCharacter()
	{
		if(cursor.getCol() <= 0)
			return Character.NEWLINE;
		return lines.get(cursor.getRow()).get(cursor.getCol() - 1);
	}
	
	private int getLineHeight(Graphics2D g2)
	{
		return Line.getHeight(g2, Configuration.fontSize) + Configuration.lineSpacing;
	}
	
	@Override
	public String toString()
	{
		return lines.stream().map(Line::toString).collect(Collectors.joining(System.lineSeparator()));
	}
}
