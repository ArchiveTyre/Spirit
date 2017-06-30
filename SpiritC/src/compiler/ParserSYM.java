package compiler;

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
@SuppressWarnings({"StatementWithEmptyBody", "SameParameterValue"})
public class ParserSYM
{
	private Lexer lexer;
	private Token[] lookAheads = new Token[3];
	private ArrayList<ASTVariableDeclaration> freeArgs = new ArrayList<>();

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

	private SpiritType parseType(ASTParent perspective)
	{
		String name = grab();

		ASTBase f = perspective.findSymbol(name);
		if (f instanceof ASTClass)
			return (ASTClass) f;

		SpiritType type = Builtins.getBuiltin(name);
		if (type != null)
			return type;

		throw new RuntimeException("Malformed .sym file. Please delete or fix it!");
	}

	private boolean parseLine(ASTClass dest)
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
				// Skip these values. They are not important right now. ^w^ //

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
				// Make sure to extend this class... //

				dest.extendsClass = grab();
				break;
			}
			case "Arg":
			{
				// Appends an argument to the argument pool. Later used by a function declaration. //

				String name = grab();
				SpiritType spiritType = parseType(dest);
				// FIXME:  Should args really have no parent?
				freeArgs.add(new ASTVariableDeclaration(null, name, spiritType, null));
				break;
			}
			case "Fun":
			{
				String name = grab();
				SpiritType spiritType = parseType(dest);
				ASTVariableDeclaration varDecl = new ASTVariableDeclaration(dest, name, Builtins.getBuiltin("function"), null);
				ASTFunctionGroup group = new ASTFunctionGroup(varDecl, name);
				ASTFunctionDeclaration fun = new ASTFunctionDeclaration(group, spiritType);
				fun.args = freeArgs;
				freeArgs = new ArrayList<>();
				//for (ASTVariableDeclaration var : fun.args)
				//	var.setParent(fun);

				break;
			}
			case "Var":
			{
				String name = grab();
				SpiritType spiritType = parseType(dest);
				new ASTVariableDeclaration(dest, name, spiritType, null);
				break;
			}
			case "Dependency":
			{
				StringBuilder dependencyName = new StringBuilder();
				while(lookAheads[0].value.equals(".") || lookAheads[0].tokenType == TokenType.SYMBOL)
				{
					dependencyName.append(grab());
				}
				dest.importClass(dependencyName.toString(), new String[] {"*"});
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
