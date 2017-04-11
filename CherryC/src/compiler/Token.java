package compiler;

/**
 * Created by tyrerexus on 11/04/17.
 */
public class Token
{
	public enum TokenType
	{
		UNKNOWN,
		KEYWORD,
		OPERATOR,
		SYMBOL,
		STRING,
		NUMBER,
		NEWLINE,
		INDENT,
		LPAR,
		RPAR,
		EOF,
	}

	public String value;
	public TokenType tokenType;


	public int columnNumber;
	public int lineNumber;

	public Token(String value, TokenType tokenType, int columnNumber, int lineNumber)
	{
		this.value = value;
		this.tokenType = tokenType;
		this.columnNumber = columnNumber;
		this.lineNumber = lineNumber;
	}
}