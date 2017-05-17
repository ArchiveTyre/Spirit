package compiler;

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
@SuppressWarnings("WeakerAccess")
public class Lexer
{
	/**
	 * The filename we are reading from.
	 */
	private String fileName;

	/**
	 * Current input stream.
	 */
	private PushbackInputStream input;

	/**
	 * The column that we are on right now.
	 */
	private int columnNumber = 0;

	/**
	 * The column that we were on when we were on the previous line.
	 */
	private int oldColumnNumber = 0;

	/**
	 * Current line number.
	 */
	private int lineNumber = 0;

	/**
	 * How many parentheses are we inside of?
	 */
	private int parenthesesCount = 0;

	/**
	 * Getter for fileName
	 * @return The filename we are reading from.
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Getter for lineNumber.
	 * @return Returns the current line.
	 */
	public int getLineNumber()
	{
		return lineNumber;
	}

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

	/**
	 * Extracts one token and moves on.
	 * @return The extracted token.
	 */
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

		// Check a bunch of single char tokens. //
		//if (c == '\n' && parenthesesCount == 0)
		//{
		//	return new Token("\n", Token.TokenType.NEWLINE, columnNumber, lineNumber);
		//}

		//while (c == ' ' || c == '\t' || c == '\n')
		//	c = readChar();

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

		// Check if we are reading a macro statement. //
		else if (c == Syntax.Macro.IDENTIFIER)
		{
			String macro = getToken().value;
			System.out.println("FOUND #");
			// Check if we have found an inline statement. //
			if (macro.equals(Syntax.Macro.INLINE))
			{

				StringBuilder inlineCode = new StringBuilder();
				// We have found an inline statement. //
				boolean inline = true;
				while (inline)
				{
					c = readChar();
					if (c == Syntax.Macro.IDENTIFIER)
					{
						 StringBuilder inlineMacro = new StringBuilder();
						 while (Character.isAlphabetic((c = readChar())))
						 {
							inlineMacro.append((char) c);
						 }

						 if (inlineMacro.toString().equals(Syntax.Macro.END))
						 {
						 	inline = false;
						 }
						 else
						 {
						 	unReadChar(c);
						 	inlineCode.append(inlineMacro.toString());
						 }
					}

					System.out.println("appending: \"" + ((char) c) + "\", " + c);
					inlineCode.append((char) c);

				}
				return new Token(inlineCode.toString(), Token.TokenType.INLINE, columnNumber, lineNumber);
			}
			else
			{
				return null;
			}
		}

		// Check if we are reading an inline comment. //
		else if (c == Syntax.Op.INLINE_COMMENT)
		{
			// Ignore the rest of the line. //
			do
			{
				c = readChar();
			} while (c != '\n' && c != -1);

			// Since we just read one char (too much), we unget it. //
			unReadChar(c);


			return getToken();
		}


		// Otherwise it's an operator. //
		else
		{
			StringBuilder operator = new StringBuilder();
			while (!Character.isLetterOrDigit((char)c) && c != ' ' && c != '\n' && c != '\t' && c!= '(' && c != ')' && c != -1)
			{
				operator.append((char) c);
				c = readChar();
			}
			unReadChar(c);

			return new Token(operator.toString(), Token.TokenType.OPERATOR, columnNumber, lineNumber);
		}
	}

	/**
	 * Gets one character from stream.
	 * @return The character that was read.
	 */
	private int readChar()
	{
		int c;

		try
		{
			c =  input.read();

			if (c == '\n')
			{
				lineNumber++;
				columnNumber = 0;
				oldColumnNumber = columnNumber;
			}
		}
		catch (IOException e)
		{
			c = -1;
			e.printStackTrace();
		}


		// Check if we are reading a block comment. //
		if (c == Syntax.Op.BLOCK_COMMENT_START.toCharArray()[0])
		{

			int temp = readChar();


			// Check if we have read a block comment (this assumes that the block comment is 2 chars in length. //
			if (temp == Syntax.Op.BLOCK_COMMENT_START.toCharArray()[1])
			{
				c = temp;
				int nestedLevel = 1;
				while (nestedLevel != 0)
				{
					c = readChar();

					if (c == -1)
					{
						return -1;
					}

					if (c == Syntax.Op.BLOCK_COMMENT_START.toCharArray()[0])
					{
						int lastSChar = c;
						c = readChar();
						if (c == Syntax.Op.BLOCK_COMMENT_START.toCharArray()[1])
						{
							nestedLevel++;
						}
						else
						{
							unReadChar(lastSChar);
							unReadChar(c);
						}

					}

					if (c == Syntax.Op.BLOCK_COMMENT_END.toCharArray()[0])
					{
						c = readChar();
						int lastEndChar = c;
						if (c == Syntax.Op.BLOCK_COMMENT_END.toCharArray()[1])
						{
							nestedLevel--;
						}
						else
						{
							unReadChar(lastEndChar);
							unReadChar(c);
						}
					}
				}
				c = readChar();
			}
			else
			{
				unReadChar(temp);
			}
		}

		return c;
	}

	/**
	 * Puts a char back onto the stream and reverts
	 * lineNumber and columnNumber to old state.
	 * @param c The character to revert.
	 */
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


