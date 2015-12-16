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
    
    public int getRow()
    {
        return row;
    }
    
    public int getCol()
    {
        return col;
    }
    
    public Cursor right(int maxRow, int maxCol)
    {
        if(col + 1 > maxCol && row < maxRow)
        {
            col = 0;
            row++;
        }
        else if(col + 1 <= maxCol)
        {
            col++;
        }
        
        return this; 
    }
    
    public Cursor down(int maxRow, int nextLineMaxCol)
    {
        if(row + 1 < maxRow)
        {
            row++;
            if(col > nextLineMaxCol)
                col = nextLineMaxCol;
        }
        return this;
    }
    
    public Cursor up(int prevLineMaxCol)
    {
        if(row - 1 >= 0)
        {
            row--;
            if(col > prevLineMaxCol)
                col = prevLineMaxCol;
        }
        return this;
    }
    
    public Cursor left(int prevLineMaxCol)
    {
        if(col - 1 < 0 && row > 0)
        {
            col = prevLineMaxCol;
            row--;
        }
        else if(col - 1 >= 0)
        {
            col--;
        }
        
        return this;
    }
    
    public Cursor home()
    {
        col = 0;
        return this;
    }
    
    public Cursor end(int maxCol)
    {
        col = maxCol;
        return this;
    }
    
    public void paint(Graphics2D g, int fontSize)
    {
        g.setColor(Color.BLACK);
        int rowHeight = Line.getHeight(fontSize, g.getFontRenderContext());
        int colWidth = Character.getWidth(g, fontSize);
        g.drawLine(col * colWidth, row * rowHeight, col * colWidth, (row + 1) * rowHeight);
    }
}
