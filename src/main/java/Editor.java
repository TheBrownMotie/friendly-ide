import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class Editor extends JComponent
{
    private static final long serialVersionUID = 1L;
    private static final Color background = Color.WHITE;
    private static final int fontSize = 14;
    private static final int lineSpacing = 5;
    
    private List<Line> lines;
    private Cursor cursor;
    private Cursor highlightStart;
    
    public Editor()
    {
        lines = new ArrayList<>();
        lines.add(new Line());
        cursor = new Cursor();
        highlightStart = null;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(background);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        final int lineHeight = getLineHeight(g2) + 1;
        final int charWidth = Character.getWidth(g2, fontSize);
        final int maxDescent = Character.getMaxDescent(g2, fontSize) - 1;
        System.out.println(maxDescent);
        int y = lineHeight;
        int row = 0;
        for (Line line : lines)
        {
            int x = 0;
            int col = 0;
            for(Character character : line.characters())
            {
                boolean highlight = Cursor.isBetween(highlightStart, cursor, new Cursor(row, col));
                drawBackground(g2, x, y + maxDescent, charWidth, lineHeight, highlight ? Color.BLUE : Color.WHITE);
                character.paint(g2, fontSize, x, y);
                drawCursors(g2, x, y - lineHeight + maxDescent, lineHeight, new Cursor(row, col), cursor, highlightStart);
                
                col++;
                x += charWidth;
            }
            
            drawCursors(g2, x, y - lineHeight + maxDescent, lineHeight, new Cursor(row, col), cursor, highlightStart);
            y += lineHeight;
            row++;
        }
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
    
    public void mark()
    {
        if(highlightStart == null)
            highlightStart = new Cursor(cursor);
        else
            highlightStart = null;
    }
    
    public void type(char c)
    {
        currentLine().type(c, cursor.getCol());
        right();
    }
    
    public void enter()
    {
        Line newLine = currentLine().splitAt(cursor.getCol());
        lines.add(cursor.getRow() + 1, newLine);
        down(1);
        home();
    }
    
    public void rightToken()
    {
        Character.TokenType tokenType = currentCharacter().getTokenType();
        if(isAtEnd())
            return;
        
        if(cursor.getCol() == cols())
            right();
        while(cursor.getCol() < cols() && currentCharacter().getTokenType() == tokenType)
            right();
    }
    
    public void leftToken()
    {
        Character.TokenType tokenType = previousCharacter().getTokenType();
        if(isAtBeginning())
            return;
        
        if(cursor.getCol() == 0)
            left();
        while(cursor.getCol() > 0 && previousCharacter().getTokenType() == tokenType)
            left();
    }
    
    public void left()
    {
        cursor.left(cols(previousRow()));
    }
    
    public void right()
    {
        cursor.right(rows(), cols());
    }
    
    public void up(int numUp)
    {
        for(int i = 0; i < numUp; i++)
            cursor.up(cols(previousRow()));
    }
    
    public void down(int numDown)
    {
        for(int i = 0; i < numDown; i++)
            cursor.down(rows(), cols(nextRow()));
    }
    
    public void home()
    {
        cursor.home();
    }
    
    public void end()
    {
        cursor.end(cols());
    }
    
    public void backspace()
    {
        cursor.left(cols(previousRow()));
        delete();
    }
    
    public void delete()
    {
        if (cols() == cursor.getCol() && currentRow() < lines.size() - 1)
            lines.set(currentRow(), new Line(currentLine(), lines.remove(nextRow())));
        else if(!isAtEnd())
            currentLine().remove(cursor.getCol());
    }
    
    public Cursor getTextCursor()
    {
        return cursor;
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
        return Line.getHeight(g2, fontSize) + lineSpacing;
    }
}
