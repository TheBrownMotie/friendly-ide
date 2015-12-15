import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class Line
{
    private List<Character> characters;
    
    public Line()
    {
        this.characters = new ArrayList<>();
    }
    
    public void paint(Graphics2D g, int fontSize, int x, int y)
    {
        int charWidth = Character.getWidth(g, fontSize);
        
        for(Character character : characters)
            character.paint(g, fontSize, x += charWidth, y);
    }
    
    public static int getHeight(int fontSize, FontRenderContext context)
    {
        return (int)new Font(Font.MONOSPACED, Font.PLAIN, fontSize).getLineMetrics(" ", context).getHeight();
    }
    
    public String toString()
    {
        return characters.stream().map(Character::toString).collect(Collectors.joining());
    }
}
