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
    
    public Line(Line line1, Line line2)
    {
        this.characters = new ArrayList<>(line1.characters);
        this.characters.addAll(line2.characters);
    }
    
    public Line subLine(int start, int end)
    {
        List<Character> sublist = characters.subList(start, end);
        Line line = new Line();
        line.characters = sublist;
        return line;
    }
    
    public Line subLine(int start)
    {
        return subLine(start, characters.size());
    }

    public int size()
    {
        return characters.size();
    }
    
    public List<Character> characters()
    {
        return characters;
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

    public Character get(int col)
    {
        return characters.get(col);
    }
}
