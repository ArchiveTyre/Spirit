package compiler.lib;

import java.io.PrintStream;

/**
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class DebugPrinter
{
	private PrintStream destination;
	public int indentation;
	private boolean new_line_clean = true;

	public DebugPrinter(PrintStream destination)
	{
		this.destination = destination;
	}

	public void println(Object what)
	{
		for (int i = 0; i < indentation; i++)
		{
			destination.print("  ");
		}
		destination.println(what);
		new_line_clean = true;
	}

	public void println()
	{
		println("");
	}

	public void print(Object what)
	{
		if (new_line_clean)
		{
			for (int i = 0; i < indentation; i++)
			{
				destination.print("  ");
			}
		}
		destination.print(what);
		new_line_clean = false;
	}
}
