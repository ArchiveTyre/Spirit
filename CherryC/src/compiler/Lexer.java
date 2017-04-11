package compiler;

import compiler.lib.Utils;

import java.io.IOException;
import java.io.PushbackInputStream;



/**
 * Created by david on 4/11/17.
 * Added getToken() by tyrerexus on 4/11/17
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

		// Check if char is -1. //
		if (c == -1)
		{
			return new Token("", Token.TokenType.EOF, columnNumber, lineNumber);
		}

		// Remove leading whitespace. //
		while (c == ' ' || c == '\t')
			c = readChar();

		if (c == '\n')
			return new Token("\n", Token.TokenType.NEWLINE, columnNumber, lineNumber);
		else if (c == '\t')
			return new Token("\t", Token.TokenType.INDENT, columnNumber, lineNumber);
		else if (c == '(')
			return new Token("(", Token.TokenType.LPAR, columnNumber, lineNumber);
		else if (c == ')')
			return new Token(")", Token.TokenType.RPAR, columnNumber, lineNumber);
		else if (Utils.isDigit(c))
		{
			String digit = "";

			while (Character.isDigit(c) && (int) c != -1)
			{
				digit += c;
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

			return new Token(digit, Token.TokenType.NUMBER, columnNumber, lineNumber);
		}
		else if (Utils.isAlpha(c) || c == '_')
		{
			String string = "";


			while ((Character.isAlphabetic(c) || Character.isDigit(c)  || c == '_') && c != -1)
			{
				string += c;
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

			return new Token(string, Token.TokenType.SYMBOL, columnNumber, lineNumber);
		}
		else
		{
			String operator = "";
			while (!Utils.isAlphaNum(c) && c != ' ' && c != '\t' && c != -1)
			{
				operator += c;
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

			return new Token(operator, Token.TokenType.OPERATOR, columnNumber, lineNumber);
		}

		// TODO: Not implemented.
		//return new Token("", Token.TokenType.UNKNOWN, 0, 0);
	}

	public char readChar()
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

	public void unReadChar(char c)
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


