import java.awt.Color;
import java.awt.Graphics2D;
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
    
    private Line(List<Character> chars)
    {
        this.characters = new ArrayList<>(chars);
    }
    
    public int size()
    {
        return characters.size();
    }
    
    public void paint(Graphics2D g, int fontSize, int x, int y)
    {
        int charWidth = Character.getWidth(g, fontSize);
        
        x -= charWidth;
        for(Character character : characters)
            character.paint(g, fontSize, x += charWidth, y);
    }
    
    public static int getHeight(Graphics2D g, int fontSize)
    {
        return g.getFontMetrics().getHeight() / 2;
    }
    
    public String toString()
    {
        return characters.stream().map(Character::toString).collect(Collectors.joining());
    }

    public void type(char c, int col)
    {
        Character character = new Character(c, Color.BLACK, Color.WHITE, false, false, false);
        if(col <= characters.size())
            characters.add(col, character);
    }
    
    public void remove(int col)
    {
        characters.remove(col);
    }
    
    public Line splitAt(int col)
    {
        List<Character> newLineCharacters = characters.subList(col, characters.size());
        this.characters = characters.subList(0, col);
        return new Line(newLineCharacters);
    }
}
