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
	private PushbackInputStream input;

	private int columnNumber = 0;
	private int oldColumnNumber = 0;
	private int lineNumber = 0;

	public Lexer(PushbackInputStream input)
	{
		this.input = input;
	}



	public Token getToken()
	{
		char c = readChar();

		// Check if char is EOF. //
		if (c == -1)
		{
			return new Token("", Token.TokenType.EOF, columnNumber, lineNumber);
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
		else if (Utils.isDigit(c))
		{
			StringBuilder digit = new StringBuilder();

			while (Character.isDigit(c) && (int) c != -1)
			{
				digit.append(c);
				c = readChar();
				try
				{
					if (input.available() == 0)
						break;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			unReadChar(c);

			return new Token(digit.toString(), Token.TokenType.NUMBER, columnNumber, lineNumber);
		}

		// check if we are reading a symbol. //
		else if (Utils.isAlpha(c) || c == '_')
		{
			StringBuilder string = new StringBuilder();


			while ((Character.isAlphabetic(c) || Character.isDigit(c)  || c == '_') && c != -1)
			{
				string.append(c);
				c = readChar();

				try
				{
					if (input.available() == 0)
						break;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			unReadChar(c);

			return new Token(string.toString(), Token.TokenType.SYMBOL, columnNumber, lineNumber);
		}

		// Otherwise it's an operator. //
		else
		{
			StringBuilder operator = new StringBuilder();
			while (!Utils.isAlphaNum(c) && c != ' ' && c != '\t' && c != -1)
			{
				operator.append(c);
				c = readChar();
				try
				{
					if (input.available() == 0)
						break;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			unReadChar(c);

			return new Token(operator.toString(), Token.TokenType.OPERATOR, columnNumber, lineNumber);
		}
	}

	private char readChar()
	{
		char character = (char) -1;
		try
		{
			character = (char) input.read();

			if (character == '\n')
			{
				lineNumber++;
				columnNumber = 0;
				oldColumnNumber = columnNumber;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return character;


	}

	private void unReadChar(char c)
	{
		try
		{
			input.unread((int) c);
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


