package compiler;

import compiler.ast.*;
import compiler.builtins.Builtins;

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

	public Parser(Lexer lexer)
	{
		this.lexer = lexer;
		look_ahead = lexer.getToken();
	}

	// FIXME: Not implemented yet.
	private ASTBase parseExpression(ASTParent parent)
	{
		if (match(TokenType.NUMBER) || match(TokenType.SYMBOL))
		{
			ASTBase left = null;
			if (previous.tokenType == TokenType.NUMBER)
			{
				left = new ASTNumber(parent, Integer.parseInt(previous.value));
			}
			else
			{
				left = new ASTVariableUsage(parent, previous.value);
			}
			if (match(TokenType.OPERATOR))
			{
				ASTFunctionCall operatorCall = new ASTFunctionCall(parent, previous.value);
				operatorCall.infix = true;
				ASTBase right = parseExpression(parent);

				left.setParent(operatorCall);
				right.setParent(operatorCall);

				return operatorCall;
			}
			else if (match(TokenType.NEWLINE) || match(TokenType.EOF))
			{
				return left;
			}

		}


		return null;
	}

	private CherryType parseType(ASTParent parent)
	{
		if (match(Syntax.OPERATOR_BLOCKSTART))
		{
			if (match(TokenType.SYMBOL))
			{
				ASTBase f = parent.findSymbol(previous.value);
				if (f instanceof ASTClass)
				{
					return (ASTClass) f;
				}

				CherryType type = Builtins.getBuiltin(previous.value);
				if (type != null)
					return type;
			}
		}
		return null;
	}

	private boolean parseLine(ASTClass dest)
	{


		// Extract indents to get a parent. //
		int line_indent = 0;
		if (match(TokenType.INDENT))
		{
			line_indent = previous.indent;
		}

		// Skip any empty lines.
		if (match(TokenType.NEWLINE))
		{
			return true;
		}

		// Use the indent to find a new parent for the contents of this line. //
		ASTParent parent = dest.getParentForNewCode(line_indent);

		// Check if we are defining a variable. //
		if (match(Syntax.KEYWORD_VAR))
		{
			if (match(TokenType.SYMBOL))
			{
				String name = previous.value;

				// Parse ": Cat". //
				CherryType definedType = parseType(parent);

				// Will be set to the initial value of this variable. //
				ASTBase value = null;

				// Parse the initial value. //
				if (match("="))
				{
					value = parseExpression(parent);
					CherryType valueType = value.getExpressionType();
					if (definedType == null)
						definedType = valueType;
					else if (definedType != valueType)
						System.err.print("ERROR: Type miss-match at line: " + previous.lineNumber);
				}

				ASTVariableDeclaration variable = new ASTVariableDeclaration(parent, name, definedType, value);
				return variable != null;
			}
			else
			{
				error("symbol", "Syntax: var <VARIABLE NAME> [ = <INITIAL VALUE>]");
				return false;
			}
		}
		else
		{
			ASTBase expression = parseExpression(parent);
			return expression != null;
		}
	}

	/**
	 * Parses the whole content of the Lexer
	 *
	 * It does this by calling parseLine as many times as possible.
	 */
	public void parseFile(ASTClass dest)
	{

		// Parse as many lines as possible. //
		while (parseLine(dest));

		// Check for garbage. //
		if (!match(TokenType.EOF))
		{
			error("end of file", "There is un-parsed junk at the end of the file. ");
		}

	}

	private boolean match(String value)
	{
		if (value.equals(look_ahead.value))
		{
			previous = look_ahead;
			look_ahead = lexer.getToken();
			return true;
		}

		return false;
	}

	private boolean match(Token.TokenType value)
	{
		if (value == look_ahead.tokenType)
		{
			previous = look_ahead;
			look_ahead = lexer.getToken();
			return true;
		}

		return false;
	}

	private void error(String expected, String message)
	{
		System.err.println("[Cherry]: Error in file: " + lexer.fileName + "\t at line " + previous.lineNumber + ".");
		System.out.println("\tExpected:\t\t" + expected);
		System.out.println("\tActual:\t\t" + previous.value);
		System.out.println("\tMessage: " + (message.equals("") ? "[NONE]" : message));
	}

}
