package compiler;

import java.util.HashMap;

/**
 * @author Tyrerexus
 * @date 11/04/17.
 */
@SuppressWarnings("WeakerAccess")
public class Token
{
	@SuppressWarnings("SpellCheckingInspection")
	public enum TokenType
	{
		UNKNOWN,
		//KEYWORD,
		OPERATOR,
		SYMBOL,
		STRING,
		NUMBER,
		NEWLINE,
		INDENT,
		LPAR,
		RPAR,
		EOF,
		INLINE;


		public static boolean isFundamental(TokenType type)
		{
			switch (type)
			{
				case STRING:
				case NUMBER:
					return true;
			}
			return false;
		}
	}


	public String value;
	public TokenType tokenType;
	public int indent = 0;


	public int columnNumber;
	public int lineNumber;

	public Token(String value, TokenType tokenType, int columnNumber, int lineNumber)
	{
		this.value = value;
		this.tokenType = tokenType;
		this.columnNumber = columnNumber;
		this.lineNumber = lineNumber;


		if (tokenType == TokenType.SYMBOL)
		{
			if (Syntax.isOperator(value))
			{
				this.tokenType = TokenType.OPERATOR;
				this.value = Syntax.getOperator(value);
			}
		}

	}

	public Token(int indent, int lineNumber)
	{
		this("", TokenType.INDENT, 0, lineNumber);
		this.indent = indent;
	}
}