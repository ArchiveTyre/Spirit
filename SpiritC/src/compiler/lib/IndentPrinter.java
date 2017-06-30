package compiler.lib;

import java.io.PrintStream;

/**
 * This class can print strings and objects while indenting lines by a counter.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class IndentPrinter
{
	/**
	 * On what stream to print to.
	 */
	private PrintStream destination;

	/**
	 * True if nothing has been written since new line.
	 */
	private boolean new_line_clean = true;

	/**
	 * The current indention of the IndentPrinter.
	 */
	public int indentation;

	public IndentPrinter(PrintStream destination)
	{
		this.destination = destination;
	}

	/**
	 * Prints the object and then prints a newline.
	 * @param what The object to print.
	 */
	public void println(Object what)
	{
		if (new_line_clean)
		{
			for (int i = 0; i < indentation; i++)
			{
				destination.print("  ");
			}
		}
		destination.println(what);
		new_line_clean = true;
	}

	/**
	 * Just prints a newline.
	 */
	public void println()
	{
		println("");
	}

	/**
	 * Prints out an object.
	 * @param what The object to print.
	 */
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
