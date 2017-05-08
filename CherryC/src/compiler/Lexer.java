package compiler;

import compiler.lib.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to extract tokens out of a stream.
 *
 * @author david
 * @date 4/11/17.
 */
public class Lexer
{

	public String fileName;

	private PushbackInputStream input;

	private int columnNumber = 0;
	private int oldColumnNumber = 0;
	private int lineNumber = 0;
	private int parenthesesCount = 0;

	private boolean inMacro = false;

	public Lexer(PushbackInputStream input, String fileName)
	{
		this.input = input;
		this.fileName = fileName;
	}

	public Lexer(String input, String fileName)
	{
		this(new PushbackInputStream (
				new ByteArrayInputStream (
						input.getBytes(StandardCharsets.UTF_8))),
				fileName);
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	public Token getToken()
	{
		int c = readChar();


		// Check if char is EOF. //
		if (c == -1)
		{
			return new Token("", Token.TokenType.EOF, columnNumber, lineNumber);
		}

		// Try to create indent token. //
		if (parenthesesCount == 0 && columnNumber == 0 && (c == ' ' || c == '\t'))
		{
			int indent = 0;
			while (c == ' ' || c == '\t')
			{
				indent += (c == ' ') ? 1 : 4;
				c = readChar();
			}
			unReadChar(c);
			return new Token(indent, lineNumber);
		}

		// Check a bunch of single char tokens. //
		if (c == '\n' && parenthesesCount == 0)
		{
			return new Token("\n", Token.TokenType.NEWLINE, columnNumber, lineNumber);
		}

		while (c == ' ' || c == '\t' || c == '\n')
			c = readChar();

		if (c == '(')
		{
			++parenthesesCount;
			return new Token("(", Token.TokenType.LPAR, columnNumber, lineNumber);
		}
		else if (c == ')')
		{
			--parenthesesCount;
			return new Token(")", Token.TokenType.RPAR, columnNumber, lineNumber);
		}
		else if (c == ':')
			return new Token(":", Token.TokenType.OPERATOR, columnNumber, lineNumber);

		// Check if we are reading a number. //
		else if (Character.isDigit((char) c))
		{
			StringBuilder digit = new StringBuilder();

			while (Character.isDigit(c) && c != -1)
			{
				digit.append((char) c);
				c = readChar();
			}
			unReadChar(c);

			return new Token(digit.toString(), Token.TokenType.NUMBER, columnNumber, lineNumber);
		}


		// check if we are reading a symbol. //
		else if (Character.isAlphabetic((char) c) || c == '_')
		{
			StringBuilder string = new StringBuilder();


			while ((Character.isAlphabetic(c) || Character.isDigit(c)  || c == '_') && c != -1)
			{
				string.append((char) c);
				c = readChar();

			}

			unReadChar(c);
			return new Token(string.toString(), Token.TokenType.SYMBOL, columnNumber, lineNumber);
		}

		// Check if we are reading a string. //
		else if (c == '"')
		{
			StringBuilder string = new StringBuilder();
			c = readChar();
			while (c != '"')
			{
				if (c != '\t' && c != '\n') string.append((char) c);
				c = readChar();
			}

			return new Token(string.toString(), Token.TokenType.STRING, columnNumber, lineNumber);
		}

		// Otherwise it's an operator. //
		else
		{
			StringBuilder operator = new StringBuilder();
			while (!Utils.isAlphaNum((char) c) && c != ' ' && c != '\n' && c != '\t' && c!= '(' && c != ')' && c != -1)
			{
				operator.append((char) c);
				c = readChar();
			}
			unReadChar(c);

			return new Token(operator.toString(), Token.TokenType.OPERATOR, columnNumber, lineNumber);
		}
	}

	private int readChar()
	{
		int character;

		try
		{
			character =  input.read();

			if (character == '\n')
			{
				lineNumber++;
				columnNumber = 0;
				oldColumnNumber = columnNumber;
			}
		}
		catch (IOException e)
		{
			character = -1;
			e.printStackTrace();
		}


		return character;


	}

	private void unReadChar(int c)
	{
		try
		{
			if (c != -1)
				input.unread(c);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		columnNumber--;


		if (c == '\n')
		{
			columnNumber = oldColumnNumber;
			lineNumber--;
		}
	}
}


