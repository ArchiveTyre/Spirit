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
		NUMBER
	}

	public String value;
	public TokenType tokenType;


	public int column_no;
	public int line_no;

	public Token(String value, TokenType tokenType, int column_no, int line_no)
	{
		this.value = value;
		this.tokenType = tokenType;
		this.column_no = column_no;
		this.line_no = line_no;
	}
}