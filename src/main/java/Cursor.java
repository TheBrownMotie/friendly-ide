import java.awt.Color;
import java.awt.Graphics2D;


public class Cursor
{
    private int row, col;
    
    public Cursor()
    {
        this.row = 0;
        this.col = 0;
    }
    
    public int getRow() { return row;  }
    public int getCol() { return col; }
    
    public Cursor right()   { col++; return this; }
    public Cursor down()    { row++; return this; }
    public Cursor up()      { row--; return this; }
    public Cursor left()    { col--; return this; }
    
    public Cursor home()    { col = 0; return this; }
    
    public void paint(Graphics2D g, int fontSize)
    {
        g.setColor(Color.BLACK);
        int rowHeight = Line.getHeight(fontSize, g.getFontRenderContext());
        int colWidth = Character.getWidth(g, fontSize);
        g.drawLine(col * colWidth, row * rowHeight, col * colWidth, (row + 1) * rowHeight);
    }
}
