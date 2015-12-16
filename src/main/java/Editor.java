import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;


public class Editor extends JComponent
{
    private static final long serialVersionUID = 1L;
    private static final int fontSize = 14;
    
    private List<Line> lines;
    private Cursor cursor;
    
    public Editor()
    {
        lines = new ArrayList<>();
        lines.add(new Line());
        cursor = new Cursor();
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
    
    public void left()
    {
        cursor.left(cols(cursor.getRow()-1));
    }

    public void right()
    {
        cursor.right(rows(), cols(cursor.getRow()));
    }
    
    public void up()
    {
        cursor.up(cols(cursor.getRow()-1));
    }
    
    public void down()
    {
        cursor.down(rows(), cols(cursor.getRow()+1));
    }
    
    public void home()
    {
        cursor.home();
    }
    
    public void end()
    {
        cursor.end(cols(cursor.getRow()));
    }

    public void delete()
    {
        lines.get(cursor.getRow()).remove(cursor.getCol());
    }
    
    private int rows() { return lines.size(); }
    private int cols(int row) { return row < 0 || row >= rows() ? 0 : lines.get(row).size(); }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        int y = 0;
        int lineHeight = Line.getHeight(fontSize, g2.getFontRenderContext());
        for(Line line : lines)
            line.paint(g2, fontSize, 0, y += lineHeight);
        cursor.paint(g2, fontSize);
    }
    
    public Cursor getTextCursor()
    {
        return cursor;
    }
}
