package compiler.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by david on 4/11/17.
 */
public class FileHandler
{

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
}
