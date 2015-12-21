package com.nickww.friendlyide;
import java.util.Objects;


public class Cursor implements Comparable<Cursor>
{
	private int row, col;
	
	public Cursor()
	{
		this.row = 0;
		this.col = 0;
	}
	
	public Cursor(Cursor that)
	{
		this.row = that.row;
		this.col = that.col;
	}
	
	public Cursor(int row, int col)
	{
		this.row = row;
		this.col = col;
	}
	
	public static boolean isBetween(Cursor c1, Cursor c2, Cursor test)
	{
		if(c1 == null || c2 == null || test == null)
			return false;
		return new CursorRange(c1, c2).contains(test);
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
		if(col + 1 > maxCol && row < maxRow - 1)
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

	@Override
	public int compareTo(Cursor that)
	{
		if(this.row != that.row)
			return Integer.compare(this.row, that.row);
		return Integer.compare(this.col, that.col);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(!(o instanceof Cursor))
			return false;
		Cursor that = (Cursor)o;
		return this.row == that.row && this.col == that.col;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(this.row, this.col);
	}
	
	@Override
	public String toString()
	{
		return "(" + row + ", " + col + ")";
	}
}
