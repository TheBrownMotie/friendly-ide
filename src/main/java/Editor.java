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
        for(Line line : lines)
            line.paint(g2, fontSize, 0, y += lineHeight);
        
        final int colWidth = getColWidth(g2);
        g.setColor(Color.BLACK);
        g.drawLine(cursor.getCol() * colWidth, cursor.getRow() * lineHeight, cursor.getCol() * colWidth, (cursor.getRow() + 1) * lineHeight);
    }
    
    public void type(char c)
    {
        lines.get(cursor.getRow()).type(c, cursor.getCol());
        right();
    }
    
    public void enter()
    {
        Line newLine = lines.get(cursor.getRow()).splitAt(cursor.getCol());
        lines.add(cursor.getRow()+1, newLine);
        down();
        home();
    }
    
    public void left() { cursor.left(cols(cursor.getRow()-1)); }
    public void right() { cursor.right(rows(), cols(cursor.getRow())); }
    public void up() { cursor.up(cols(cursor.getRow()-1)); }
    public void down() { cursor.down(rows(), cols(cursor.getRow()+1)); }
    public void home() { cursor.home(); }
    public void end() { cursor.end(cols(cursor.getRow())); }
    public void delete() { lines.get(cursor.getRow()).remove(cursor.getCol()); }
    
    public Cursor getTextCursor() { return cursor; }
    
    private int rows() { return lines.size(); }
    private int cols(int row) { return row < 0 || row >= rows() ? 0 : lines.get(row).size(); }

    private int getLineHeight(Graphics2D g2) { return Line.getHeight(g2, fontSize) + lineSpacing; }
    private int getColWidth(Graphics2D g2) { return Character.getWidth(g2, fontSize); }
}
