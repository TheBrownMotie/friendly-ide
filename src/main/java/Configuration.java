import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Configuration
{
	public static Map<String, Color> keywordColors;
	public static final Color background = Color.WHITE;
	public static final int fontSize = 14;
	public static final int lineSpacing = 5;
    public static final int numSpacesPerTab = 4;

	public static void load(File keywordColormap) throws FileNotFoundException
	{
		keywordColors = new HashMap<>();
		try(Scanner scanner = new Scanner(keywordColormap))
		{
			while(scanner.hasNextLine())
			{
				String[] parts = scanner.nextLine().split(" ");
				keywordColors.put(parts[0], new Color(
						Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3])));
			}
		}
	}
}
