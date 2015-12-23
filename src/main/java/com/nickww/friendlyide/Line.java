package com.nickww.friendlyide;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
		
	public List<Integer> indicesOf(String search)
	{
		Pattern p = Pattern.compile("\\b" + search + "\\b");
		Matcher m = p.matcher(toString());
		List<Integer> indices = new ArrayList<>(search.length());
		while(m.find())
			indices.add(m.start());
		return indices;
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

	public Line type(char c, int col)
	{
		Character character = new Character(c, Color.BLACK, Color.WHITE, false, false, false);
		characters.forEach(ch -> ch.setFontColor(Color.BLACK));
		if(col <= characters.size())
			characters.add(col, character);
		return this;
	}
	
	public Line remove(int col)
	{
		characters.remove(col);
		return this;
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
