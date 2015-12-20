import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public final class Character
{
    public static enum TokenType
    {
        WHITESPACE, IDENTIFIER, SYMBOL;
        
        public static TokenType get(char c)
        {
            if(java.lang.Character.isWhitespace(c))
                return WHITESPACE;
            if(java.lang.Character.isJavaIdentifierPart(c))
                return IDENTIFIER;
            return SYMBOL;
        }
    }
    
    public static final Character NEWLINE = new Character('\n', null, null, false, false, false);
    
    private final char c;
    private final TokenType type;
    private Color fontColor, highlight;
    private boolean isBold, isItalics, isStrikethrough;
    
    public Character(char c, Color fontColor, Color highlight, boolean isBold, boolean isItalics, boolean isStrikethrough)
    {
        this.c = c;
        this.type = TokenType.get(c);
        
        this.fontColor = fontColor;
        this.highlight = highlight;
        this.isBold = isBold;
        this.isItalics = isItalics;
        this.isStrikethrough = isStrikethrough;
    }
    
    public TokenType getTokenType()
    {
        return type;
    }
    
    public static int getWidth(Graphics2D g, int fontSize)
    {
        return g.getFontMetrics(new Font(Font.MONOSPACED, Font.PLAIN, fontSize)).stringWidth(" ");
    }
    
    public void setFontColor(Color fontColor)
    {
        this.fontColor = fontColor;
    }

    public void setHighlight(Color highlight)
    {
        this.highlight = highlight;
    }
    
    public void setBold(boolean isBold)
    {
        this.isBold = isBold;
    }
    
    public void setItalics(boolean isItalics)
    {
        this.isItalics = isItalics;
    }
    
    public void setStrikethrough(boolean isStrikethrough)
    {
        this.isStrikethrough = isStrikethrough;
    }
    
    public void paint(Graphics2D g, int fontSize, int x, int y)
    {
        int w = Character.getWidth(g, fontSize);
        int h = Line.getHeight(g, fontSize);

        g.setFont(getFont(fontSize));
        g.setColor(highlight);
        g.fillRect(x, y - h + 1, w, h + 1);
        g.setColor(fontColor);
        g.drawString(c + "", x, y);
        
        if(isStrikethrough)
            g.drawLine(x, y - (h/2), x + w, y - (h/2));
    }
    
    public Font getFont(int size)
    {
        int style = Font.PLAIN;
        if(isBold)
            style = style | Font.BOLD;
        if(isItalics)
            style = style | Font.ITALIC;
        
        return new Font(Font.MONOSPACED, style, size);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == this)
            return true;
        if(!(o instanceof Character))
            return false;
        return ((Character)o).c == this.c;
    }
    
    @Override
    public int hashCode()
    {
        return java.lang.Character.hashCode(this.c);
    }
    
    @Override
    public String toString()
    {
        return c + "";
    }
}
