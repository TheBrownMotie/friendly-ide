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
    
    public Editor()
    {
        lines = new ArrayList<>();
        lines.add(new Line());
        cursor = new Cursor();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(background);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        int y = 0;
        final int lineHeight = getLineHeight(g2);
        for (Line line : lines)
            line.paint(g2, fontSize, 0, y += lineHeight);
        
        final int colWidth = getColWidth(g2);
        g.setColor(Color.BLACK);
        g.drawLine(cursor.getCol() * colWidth, cursor.getRow() * lineHeight, cursor.getCol() * colWidth, (cursor.getRow() + 1) * lineHeight);
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
        if(cursor.getCol() == cols())
            return;
        final Character.TokenType tokenType = currentCharacter().getTokenType();
        while(cursor.getCol() < cols() && currentCharacter().getTokenType() == tokenType)
            right();
    }
    
    public void leftToken()
    {
        if(cursor.getCol() == 0)
            return;
        final Character.TokenType tokenType = previousCharacter().getTokenType();
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
        else
            currentLine().remove(cursor.getCol());
    }
    
    public Cursor getTextCursor()
    {
        return cursor;
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
        return lines.get(cursor.getRow()).get(cursor.getCol());
    }
    
    private Character previousCharacter()
    {
        return lines.get(cursor.getRow()).get(cursor.getCol() - 1);
    }
    
    private int getLineHeight(Graphics2D g2)
    {
        return Line.getHeight(g2, fontSize) + lineSpacing;
    }
    
    private int getColWidth(Graphics2D g2)
    {
        return Character.getWidth(g2, fontSize);
    }
}
