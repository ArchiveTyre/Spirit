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

	/** The lexer to read from. */
	Lexer lexer;

	private Token lookAhead;
	private Token previous = null;

	/**
	 * Creates a Parser that will read from a lexer.
	 * @param lexer The lexer to read from.
	 */
	public Parser(Lexer lexer)
	{
		this.lexer = lexer;
		lookAhead = lexer.getToken();
	}

	// FIXME: Add support for strings.
	private ASTBase parseExpression(ASTParent parent)
	{
		if (match(TokenType.NUMBER) || match(TokenType.SYMBOL))
		{
			ASTBase left;

			// Create left-hand side. //
			if (previous.tokenType == TokenType.NUMBER)
			{
				left = new ASTNumber(parent, Integer.parseInt(previous.value));
			}
			else if (previous.tokenType == TokenType.SYMBOL)
			{
				String name = previous.value;

				// Check if function call. //
				if (match(TokenType.LPAR))
				{
					ASTFunctionCall functionCall = new ASTFunctionCall(parent, name);

					// Try to parse arguments. //
					if (lookAhead.tokenType != TokenType.RPAR)
					{
						do
						{
							parseExpression(functionCall);
						} while (match(","));

						if (!match(TokenType.RPAR))
						{
							error(")", "Unmatched parenthesis");
						}
					}
					return functionCall;
				}

				// Single argument function call. //
				else if (lookAhead.tokenType == TokenType.SYMBOL
						|| lookAhead.tokenType == TokenType.STRING
						|| lookAhead.tokenType == TokenType.NUMBER)
				{
					ASTFunctionCall functionCall = new ASTFunctionCall(parent, name);
					parseExpression(functionCall);
					return functionCall;
				}

				// Nope, just normal symbol. //
				else
				{
					left = new ASTVariableUsage(parent, name);
				}
			}

			// An error has occurred. //
			else
			{
				System.err.println("Compiler error.");
				return null;
			}

			// Check if we have hit an end. //
			if (lookAhead.value.equals(",") || lookAhead.value.equals(":"))
			{
				return left;
			}

			// Either we have an operator, or alternatively left is single-node expression or a function call. //
			else if (match(TokenType.OPERATOR))
			{
				ASTFunctionCall operatorCall = new ASTFunctionCall(parent, previous.value);
				operatorCall.infix = true;
				ASTBase right = parseExpression(parent);

				left.setParent(operatorCall);
				right.setParent(operatorCall);

				return operatorCall;
			}

			// Single-node expression. //
			else if (match(TokenType.NEWLINE)
					|| match(TokenType.EOF)
					|| lookAhead.tokenType == TokenType.RPAR)
			{
				return left;
			}
			else
			{
				error("end of expression", "");
			}
		}

		// Try to parse parentheses. //
		else if (match(TokenType.LPAR))
		{

			ASTBase expression =  parseExpression(parent);
			if (match(TokenType.RPAR))
			{
				return expression;
			}
			else
			{
				error(")", "Unmatched parenthesis");
			}
		}

		return null;
	}

	/**
	 * Mainly used by parseFunctionDeclaration() and parseVariableDeclaration().
	 * @param perspective From what perspective to search from.
	 * @return A type.
	 */
	private CherryType parseType(ASTParent perspective)
	{
		if (match(Syntax.OPERATOR_BLOCKSTART))
		{
			if (match(TokenType.SYMBOL))
			{
				return findType(perspective, previous.value);
			}
		}
		return null;
	}

	private ASTVariableDeclaration parseFunctionDeclaration(ASTParent parent)
	{
		if (match(Syntax.KEYWORD_FUN))
		{
			if (match(TokenType.SYMBOL))
			{
				String name = previous.value;

				// Create the function declaration with the default type of void. //
				ASTFunctionDeclaration function = new ASTFunctionDeclaration(parent, Builtins.getBuiltin("void"));

				// Parse args. //
				if(match(TokenType.LPAR))
				{
					// If we aren't directly followed by a closing parentheses. //
					if (!match(TokenType.RPAR))
					{

						// Match as many arguments as possible. //
						do
						{
							if (match(TokenType.SYMBOL))
							{
								String argName = previous.value;
								CherryType argType = parseType(parent);
								// TODO: Support default value.
								function.args.add(new ASTVariableDeclaration(null, argName, argType, null));
							}
						} while (match(","));

						// Check for matching parentheses. //
						if (!match (TokenType.RPAR))
						{
							error(")", "Unmatched parenthesis");
							return null;
						}
					}
				}

				// Parse return type. //
				if (match(Syntax.OPERATOR_RETURNTYPE))
				{
					if (match(TokenType.SYMBOL))
					{
						function.returnType = findType(parent, previous.value);
					}
					else
					{
						error("return type", "Return type is required after \"->\"");
						return null;
					}
				}

				// Make sure function declarations end with a colon. //
				if (!match(Syntax.OPERATOR_BLOCKSTART))
				{
					error(":", "A colon is required at the end of a function declaration.");
				}

				return new ASTVariableDeclaration(parent, name, Builtins.getBuiltin("fun"), function);
			}
			else
			{
				error("name", "Expected a name for the function.");
				return null;
			}
		}
		else
		{
			System.err.println("Compiler error!");
			return null;
		}
	}

	private ASTVariableDeclaration parseVariableDeclaration(ASTParent parent)
	{
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

				return new ASTVariableDeclaration(parent, name, definedType, value);
			}
			else
			{
				error("symbol", "Syntax: var <VARIABLE NAME> [ = <INITIAL VALUE>]");
				return null;
			}
		}
		else
		{
			System.err.println("Compiler error!");
			return null;
		}
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
		if (lookAhead.value.equals(Syntax.KEYWORD_VAR))
		{
			ASTVariableDeclaration variable = parseVariableDeclaration(parent);
			if (variable != null)
			{
				variable.columnNumber = line_indent;
				return true;
			}

			return false;
		}

		// Check if we are defining a function. //
		else if (lookAhead.value.equals(Syntax.KEYWORD_FUN))
		{
			ASTVariableDeclaration function = parseFunctionDeclaration(parent);
			if (function != null)
			{
				function.columnNumber = line_indent;
				return true;
			}

			return false;
		}

		else if (match(Syntax.KEYWORD_IF))
		{
			ASTBase condition = parseExpression(parent);
			if (match(Syntax.OPERATOR_BLOCKSTART))
			{
				new ASTIf(parent, condition);
				return true;
			}
			else
			{
				error(":", "A colon is required at the end of an if statement.");
				return false;
			}

		}

		else if (match(Syntax.KEYWORD_ELSE))
		{
			if (match(Syntax.OPERATOR_BLOCKSTART))
			{
				new ASTElse(parent);
				return true;
			}
			else
			{
				error(":", "A colon is required at the end of an else statement.");
				return false;
			}

		}

		// Otherwise it's just an expression. //
		else
		{
			ASTBase expression = parseExpression(parent);
			if (expression != null)
				expression.columnNumber = line_indent;
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

	private CherryType findType(ASTParent perspective, String name)
	{
		// FIXME: Is this really the best place?

		ASTBase f = perspective.findSymbol(name);
		if (f instanceof ASTClass)
			return (ASTClass) f;

		CherryType type = Builtins.getBuiltin(name);
		if (type != null)
			return type;

		return null;
	}

	private boolean match(String value)
	{
		if (value.equals(lookAhead.value))
		{
			previous = lookAhead;
			lookAhead = lexer.getToken();
			return true;
		}

		return false;
	}

	private boolean match(Token.TokenType value)
	{
		if (value == lookAhead.tokenType)
		{
			previous = lookAhead;
			lookAhead = lexer.getToken();
			return true;
		}

		return false;
	}

	private void error(String expected, String message)
	{
		System.err.println("[Cherry]: Error in file: " + lexer.fileName + "\t at line " + previous.lineNumber + ".");
		System.err.println("\tExpected:\t\t" + expected);
		System.err.println("\tActual:\t\t\t" + lookAhead.value);
		System.err.println("\tMessage: " + (message.equals("") ? "[NONE]" : message));
	}

}
