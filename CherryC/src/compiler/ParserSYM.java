package compiler;

import compiler.CherryType;
import compiler.Lexer;
import compiler.Token;
import compiler.ast.*;
import compiler.builtins.Builtins;

import java.util.ArrayList;

import static compiler.Token.TokenType;

/**
 * Parses .sym files.
 *
 * @author Tyrerexus
 * @date 5/4/17.
 */
public class ParserSYM
{
	Lexer lexer;
	private Token[] lookAheads = new Token[3];

	/**
	 * Creates a Parser that will read from a lexer.
	 * @param lexer The lexer to read from.
	 */
	public ParserSYM(Lexer lexer)
	{
		this.lexer = lexer;
		for (int i = 0; i < lookAheads.length; i++)
		{
			lookAheads[i] = lexer.getToken();
		}
	}

	private CherryType parseType(ASTParent perspective)
	{
		String name = grab();

		ASTBase f = perspective.findSymbol(name);
		if (f instanceof ASTClass)
			return (ASTClass) f;

		CherryType type = Builtins.getBuiltin(name);
		if (type != null)
			return type;

		return null;
	}

	ArrayList<ASTVariableDeclaration> freeArgs = new ArrayList<>();

	public boolean parseLine(ASTClass dest)
	{
		// Skip any empty lines.
		if (match(Token.TokenType.NEWLINE))
			return true;

		if (match(Token.TokenType.EOF))
			return false;

		String type = lookAheads[0].value;
		step();
		if (!match(":"))
		{
			System.out.println("Missing colon in .sym file.");
		}
		switch (type)
		{
			case "CompilerVersion":
			case "ClassName":
			{
				while (true)
				{
					step();
					if (lookAheads[0].tokenType == TokenType.EOF
							|| lookAheads[0].tokenType == TokenType.NEWLINE)
						break;
				}
				break;
			}
			case "ExtendsClass":
			{
				dest.extendsClass = grab();
				break;
			}
			case "Arg":
			{
				String name = grab();
				CherryType cherryType = parseType(dest);
				// FIXME:  Should args really have no parent?
				freeArgs.add(new ASTVariableDeclaration(null, name, cherryType, null));
				break;
			}
			case "Fun":
			{
				String name = grab();
				CherryType cherryType = parseType(dest);
				ASTVariableDeclaration varDecl = new ASTVariableDeclaration(dest, name, Builtins.getBuiltin("function"), null);
				ASTFunctionDeclaration fun = new ASTFunctionDeclaration(varDecl, cherryType);
				fun.args = freeArgs;
				freeArgs = new ArrayList<>();
				//for (ASTVariableDeclaration var : fun.args)
				//	var.setParent(fun);

				break;
			}
			case "Var":
			{
				String name = grab();
				CherryType cherryType = parseType(dest);
				new ASTVariableDeclaration(dest, name, cherryType, null);
				break;
			}
			default:

		}
		return true;
	}

	public boolean parseFile(ASTClass dest)
	{
		while (parseLine(dest))
			;
		if (match(TokenType.EOF))
			return true;

		System.err.println("ERROR: Corrupted .sym file!");
		return false;
	}

	private void step()
	{
		//previous = lookAheads[0];
		System.arraycopy(lookAheads, 1, lookAheads, 0, lookAheads.length - 1);
		lookAheads[lookAheads.length - 1] = lexer.getToken();
	}

	private boolean match(Token.TokenType value)
	{
		if (value == lookAheads[0].tokenType)
		{
			step();
			return true;
		}
		return false;
	}

	private boolean match(String value)
	{
		if (value.equals(lookAheads[0].value))
		{
			step();
			return true;
		}
		return false;
	}

	private String grab()
	{
		String returnValue = lookAheads[0].value;
		step();
		return returnValue;
	}
}
