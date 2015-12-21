
public class CursorRange
{
	private Cursor fst;
	private Cursor snd;
	
	public CursorRange(Cursor c1, Cursor c2)
	{
		fst = c1.compareTo(c2) < 0 ? c1 : c2;
		snd = c1.compareTo(c2) < 0 ? c2 : c1;
	}
	
	public boolean contains(Cursor test)
	{
		return fst.compareTo(test) <= 0 && test.compareTo(snd) < 0;
	}
	
	public Cursor getFirst()
	{
		return fst;
	}
	
	public Cursor getSecond()
	{
		return snd;
	}
}
