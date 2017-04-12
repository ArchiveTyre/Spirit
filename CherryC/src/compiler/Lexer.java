package compiler;

import compiler.lib.Utils;
import java.io.IOException;
import java.io.PushbackInputStream;

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


	public Lexer(PushbackInputStream input, String fileName)
	{
		this.input = input;
		this.fileName = fileName;
	}



	public Token getToken()
	{
		int c = readChar();

		int indent = 0;

		// Check if char is EOF. //
		if (c == -1)
		{
			return new Token("", Token.TokenType.EOF, columnNumber, lineNumber);
		}

		if (columnNumber == 0 && (c == ' ' || c == '\t'))
		{
			while (c == ' ' || c == '\t')
			{
				indent += (c == ' ') ? 1 : 4;
				c = readChar();
			}

			return new Token(indent, lineNumber);
		}


		// Remove leading whitespace. //
		while (c == ' ' || c == '\t')
			c = readChar();

		// Check a bunch of single char tokens. //
		if (c == '\n')
			return new Token("\n", Token.TokenType.NEWLINE, columnNumber, lineNumber);
		else if (c == '\t')
			return new Token("\t", Token.TokenType.INDENT, columnNumber, lineNumber);
		else if (c == '(')
			return new Token("(", Token.TokenType.LPAR, columnNumber, lineNumber);
		else if (c == ')')
			return new Token(")", Token.TokenType.RPAR, columnNumber, lineNumber);

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

		// Otherwise it's an operator. //
		else
		{
			StringBuilder operator = new StringBuilder();
			while (!Utils.isAlphaNum((char) c) && c != ' ' && c != '\t' && c!= '(' && c != ')' && c != -1)
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


