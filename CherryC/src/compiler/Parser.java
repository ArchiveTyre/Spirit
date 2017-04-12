package compiler;

import compiler.ast.ASTBase;
import compiler.ast.ASTClass;
import compiler.ast.ASTParent;
import compiler.ast.ASTVariable;

import static compiler.Token.TokenType;

/**
 * This class uses a Lexer to build an AST.
 *
 * @author Tyrerexus
 * @date 4/11/17.
 */
public class Parser
{
	Lexer lexer;
	Token look_ahead;
	Token previous = null;

	Parser(Lexer lexer)
	{
		this.lexer = lexer;
		look_ahead = lexer.getToken();
	}

	// FIXME: Not implemented yet.
	ASTBase parseExpression(ASTParent parent)
	{
		return null;
	}

	CherryType parseType(ASTParent parent)
	{
		if (match(Syntax.OPERATOR_BLOCKSTART))
		{
			if (match(TokenType.SYMBOL))
			{
				ASTBase f = parent.findSymbol(previous.value);
				if (f instanceof ASTClass)
				{
					return (ASTClass)f;
				}
			}
		}
		return null;
	}

	boolean parseLine(ASTClass dest)
	{

		int line_indent = 0;
		if (match(TokenType.INDENT))
		{
			line_indent = previous.indent;
		}
		ASTParent parent = dest.getParentForNewCode(line_indent);

		if (match(Syntax.KEYWORD_VAR))
		{
			if (match(TokenType.SYMBOL))
			{
				String name = previous.value;

				// Parse ": Cat". //
				CherryType definedType = parseType(parent);

				ASTBase value = null;

				// Parse the initial value. //
				if (match("="))
				{
					value = parseExpression(parent);
					CherryType valueType = value.getExpressionType();
					if (definedType == null)
						definedType = valueType;
					else if (definedType != valueType)
						System.err.print("ERROR: Type missmatch!");
				}

				ASTVariable variable = new ASTVariable(parent, name, definedType, value);
				return true;
			}
		}
		return false;
	}

	/**
	 * Parses the whole content of the Lexer
	 *
	 * It does this by calling parseLine as many times as possible.
	 */
	void parseFile(ASTClass dest)
	{
		while (parseLine(dest));

	}

	boolean match(String value)
	{
			if (value.equals(look_ahead.value)) {
				previous = look_ahead;
				look_ahead = lexer.getToken();
				return true;
			}

			return false;
	}

	boolean match(Token.TokenType value)
	{
		if (value == look_ahead.tokenType) {
			previous = look_ahead;
			look_ahead = lexer.getToken();
			return true;
		}

		return false;
	}

}
