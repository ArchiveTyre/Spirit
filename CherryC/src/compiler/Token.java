package compiler;

/**
 * @author Tyrerexus
 * @date 11/04/17.
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
		INLINE,
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
	}

	public Token(int indent, int lineNumber)
	{
		this("", TokenType.INDENT, 0, lineNumber);
		this.indent = indent;
	}
}