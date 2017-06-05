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
	private String filename;
	private Parser parser;

	public ErrorPrint(Parser parser, PrintStream out)
	{
		this.parser = parser;
		this.out = out;
	}

	public void syntaxError(String expected, String actual, String message)
	{
		error("Syntax", expected, actual, message);
	}

	public void syntaxError(String expected, String message)
	{
		error("Syntax", expected, message);
	}

	public void unexpectedExpressionError(String expected, String actual, String message)
	{
		error("Unexpected Expression", expected, actual, message);
	}

	public void unexpectedExpressionError(String expected, String message)
	{
		error("Unexpected Expression", expected, message);
	}

	public void compilerError(String message)
	{
		error("Compiler", message);
	}

	/**
	 * Display an error.
	 * @param errorType	The type of the error.
	 * @param expected	What was expected from the code.
	 * @param actual	What was found.
	 * @param message	Additional message providing more information.
	 */
	@SuppressWarnings("WeakerAccess")
	public void error(String errorType, String expected, String actual, String message)
	{
		System.err.println("[Raven] " + errorType + " error in file \"" + parser.lexer.getFileName() + "\"\tat line: " + parser.lexer.getLineNumber());
		if (!expected.isEmpty()) System.err.println("\tExpected:\t\t" + expected);
		if (!actual.isEmpty()) System.err.println("\tActual:\t\t" + actual);
		if (!message.isEmpty()) System.err.println("\tMessage:\t\t" + message);

		System.exit(0);
	}

	public void error(String errorType, String expected, String message)
	{
		error(errorType, expected, parser.lookAheads[0].value, message);
	}

	public void error(String errorType, String message)
	{
		error(errorType, "", "", "");
	}
}


