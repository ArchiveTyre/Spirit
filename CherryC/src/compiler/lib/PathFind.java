package compiler.lib;

import java.io.File;

/**
 * This class can find files that are located within a path variable.
 *
 * @author Tyrerexus
 * @date 5/4/17.
 */
public class PathFind
{
	/**
	 * Finds a file in a path.
	 * @param path The path to search through.
	 * @param toFind The file to find.
	 * @return The path to the result. Null if file not found.
	 */
	public static String findInPath(String path, String toFind)
	{
		// Parse the path. //
		String[] locations = path.split(":");
		for (String location : locations)
		{
			File locationFile = new File(location);
			if (locationFile.exists() && locationFile.isDirectory())
			{
				// Try to match the location. //
				File file = new File(location+ "/" + toFind);
				if (file.exists() && !file.isDirectory())
				{
					return file.getPath();
				}
			}
		}
		return null;
	}
}
