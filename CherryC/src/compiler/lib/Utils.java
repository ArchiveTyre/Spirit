package compiler.lib;

/**
 * @author david
 * @date 4/11/17
 */
public class Utils
{

	public static boolean isAlphaNum(char c)
	{
		return Character.isAlphabetic(c) || Character.isDigit(c);
	}
}
