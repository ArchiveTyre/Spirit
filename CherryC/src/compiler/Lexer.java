package compiler;

import java.io.InputStream;

/**
 * Created by david on 4/11/17.
 * Added getToken() by tyrerexus on 4/11/17
 */
public class Lexer
{

	InputStream input;

	public Lexer(InputStream input)
	{
		this.input = input;
	}

	Token getToken()
	{
		// TODO: Not implemented.
		return new Token("", Token.TokenType.UNKNOWN, 0, 0);
	}
}
