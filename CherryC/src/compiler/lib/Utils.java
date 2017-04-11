package compiler.lib;

/**
 * Created by david on 4/11/17.
 */
public class Utils
{

	public static boolean isDigit(char c)
	{
		switch (c)
		{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				return true;
			default:
				return false;
		}
	}

	public static boolean isAlpha(char c)
	{

		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

		for (char letter : alphabet)
		{
			if (c == letter) return true;
		}

		return false;
	}

	public static boolean isAlphaNum(char c)
	{
		return isAlpha(c) || isDigit(c);
	}
}
