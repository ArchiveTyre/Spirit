package compiler.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by david on 4/11/17.
 */
public class FileHandler
{

	/**
	 * Fetches the content a file.
	 * @param fileName The name of the file to read.
	 * @return The content of the file.
	 */
	public static String getFileContents(String fileName)
	{
		try
		{
			return new String(Files.readAllBytes(Paths.get(fileName)));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates an empty file.
	 * @param fileName The name of the file.
	 * @return If the creation of the file was successful.
	 */
	public static boolean createFile(String fileName)
	{
		try
		{
			Files.write(Paths.get(fileName), new byte[]{});
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
