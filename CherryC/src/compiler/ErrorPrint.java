package compiler;

import java.io.PrintStream;

/**
 * @author david
 * @date 5/5/17
 */
@SuppressWarnings("unused")
public class ErrorPrint
{
	public PrintStream out = System.out;
	private Parser parser;

	public ErrorPrint(Parser parser)
	{

	}

	public void syntaxError(String expected, String actual, String message, boolean fatal)
	{
		error("Syntax", expected, actual, message, fatal);
	}


	public void syntaxError(String expected, String actual, String message)
	{
		error("Syntax", expected, actual, message, true);
	}


	/**
	 * Display an error.
	 * @param errorType	The type of the error.
	 * @param expected	What was expected from the code.
	 * @param actual	What was found.
	 * @param message	Additional message providing more information.
	 */
	@SuppressWarnings("WeakerAccess")
	public void error(String errorType, String expected, String actual, String message, boolean fatal)
	{
		// If the error is fatal, prepend a sign that it is fatal. //
		errorType = (fatal) ? "FATAL" + errorType : errorType;
		System.err.println("[Raven] " + errorType + "error in file \"" + parser.lexer.getFileName() + "\"\tat line: ");
		System.err.println("\tExpected:\t\t" + expected);
		System.err.println("\tActual:\t\t" + actual);
		System.err.println("\tMessage:\t\t" + (message.equals("") ? "[NONE]" : message));

		if (fatal) System.exit(0);
	}

	public void error(String errorType, String expected, String actual, String messager)
	{
		error(errorType, expected, actual, messager, true);
	}








}


