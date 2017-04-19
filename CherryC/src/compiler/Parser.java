package compiler;

import compiler.ast.*;
import compiler.builtins.Builtins;
import compiler.builtins.FileType;

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
	private Lexer lexer;

	private Token lookAhead;
	private Token nextLookAhead;
	private Token previous = null;

	private boolean fileTypeDeclared = false;
	private int parseStage = ParseStage.NONE;

	/**
	 * Creates a Parser that will read from a lexer.
	 * @param lexer The lexer to read from.
	 */
	public Parser(Lexer lexer)
	{
		this.lexer = lexer;
		lookAhead = lexer.getToken();
		nextLookAhead = lexer.getToken();
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
							syntaxError(")", "Unmatched parenthesis");
						}
					}
					return functionCall;
				}

				// Single argument function call. //
				else if ((lookAhead.tokenType == TokenType.SYMBOL && !Syntax.isKeyword(lookAhead.value))
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

			// An syntaxError has occurred. //
			else
			{
				System.err.println("Compiler syntaxError.");
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

				if (right != null)
					right.setParent(operatorCall);
				else
					return null;

				return operatorCall;
			}

			// Single-node expression. //
			else if (match(TokenType.NEWLINE)
					|| match(TokenType.EOF)
					|| lookAhead.tokenType == TokenType.RPAR
					|| Syntax.isKeyword(lookAhead.value))
			{
				return left;
			}
			else
			{
				syntaxError("end of expression", "");
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
				syntaxError(")", "Unmatched parenthesis");
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
		if (match(Syntax.OPERATOR_TYPESPECIFY))
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
							syntaxError(")", "Unmatched parenthesis");
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
						syntaxError("return type", "Return type is required after \"->\"");
						return null;
					}
				}

				// Make sure function declarations end with a colon. //
				if (!match(Syntax.OPERATOR_BLOCKSTART))
				{
					syntaxError(":", "A colon is required at the end of a function declaration.");
				}

				return new ASTVariableDeclaration(parent, name, Builtins.getBuiltin("fun"), function);
			}
			else
			{
				syntaxError("name", "Expected a name for the function.");
				return null;
			}
		}
		else
		{
			System.err.println("Compiler syntaxError!");
			return null;
		}
	}

	private ASTVariableDeclaration parseVariableDeclaration(ASTParent parent)
	{
		if (match(TokenType.SYMBOL))
		{
			String name = previous.value;
			if (lookAhead.value.equals(Syntax.OPERATOR_TYPESPECIFY))
			{
				CherryType cherryType = parseType(parent);
				ASTBase value = null;

				// Try to parse initial value. //
				if (match("="))
				{
					value = parseExpression(parent);
					if (cherryType == null)
						cherryType = value.getExpressionType();

						// Check that the types match. //
					else if (cherryType != value.getExpressionType())
						System.err.print("ERROR: Type miss-match at line: " + previous.lineNumber);
				}

				return new ASTVariableDeclaration(parent, name, cherryType, value);
			}
			else
			{
				return null;
			}
		}
		else
		{
			syntaxError("name", "Names are required for variable declaration.");
			return null;
		}
	}


	private ASTFileTypeDeclaration parseFileTypeDeclaration(ASTParent parent)
	{
		ASTFileTypeDeclaration declaration;
		if (match(TokenType.SYMBOL))
		{
			if (FileType.toFileType(previous.value) != FileType.UNDEFINED)
			{


				declaration = new ASTFileTypeDeclaration(parent, FileType.toFileType(previous.value));
				return declaration;

			}
			else
			{
				syntaxError("filetype", previous.value, "The file type provided is not recognized");
			}
		}
		else
		{
			syntaxError("file type", "A file type is required for file type declaration.");
		}

		return null;
	}

	private ASTLoop parseLoop(ASTParent parent)
	{
		if (match(Syntax.KEYWORD_LOOP))
		{
			ASTLoop loop = new ASTLoop(parent);

			if (nextLookAhead.value.equals(Syntax.OPERATOR_TYPESPECIFY))
				loop.initialStatement = parseVariableDeclaration(loop);
			else
				loop.initialStatement = parseExpression(loop);

			if (match(","))
			{

				loop.conditionalStatement = parseExpression(loop);
			}
			if (match(","))
			{
				loop.iterationalStatement = parseExpression(loop);
			}
			if (!match(Syntax.OPERATOR_BLOCKSTART))
			{
				syntaxError(":", "A colon is required at the end of a loop statement.");
			}

			return loop;
		}
		else
		{
			System.err.println("Compiler syntaxError!");
			return null;
		}
	}

	private ASTSubclassExpression parseSubclassExpression(ASTParent parent)
	{
		if (match(Syntax.KEYWORD_EXTEND))
		{
			if (match(TokenType.SYMBOL))
			{
				ASTSubclassExpression expression = new ASTSubclassExpression(parent, previous.value);
				return expression;
			}
			else
			{
				unexpectedExpressionError(lookAhead.value, "Invalid name for class/object.");
			}
		}
		else
		{
			System.err.println("Compiler syntaxError! There was no subclass expression!");
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


		// Check if it contains the keyword "type" to see if we can see what type it is. //
		if (match(Syntax.KEYWORD_TYPE))
		{
			// Check that we have not already declared the filetype. //
			if (!fileTypeDeclared)
			{
				// Get the file type declaration. //
				ASTFileTypeDeclaration declaration = parseFileTypeDeclaration(parent);
				if (declaration != null)
				{
					declaration.columnNumber = line_indent;
					fileTypeDeclared = true;

					setStage(ParseStage.FILETYPE_DECLARATION);
					return true;
				}
				return false;
			} else
			{
				// Error. //
				unexpectedExpressionError(previous.value, "File type has already been declared, was not expecting another declaration");
			}
		}

		if (parent == null)
		{
			System.err.println("Incorrect line indentation at: " + lexer.getLineNumber());
			System.err.println("Tabbing: " + line_indent);
			return false;
		}

		// Check if we are extending a class. //
		if (lookAhead.value.equals(Syntax.KEYWORD_EXTEND))
		{
			ASTSubclassExpression subclassExpression = parseSubclassExpression(parent);
			if (subclassExpression != null)
			{
				setStage(ParseStage.EXTENSIONS);
				subclassExpression.columnNumber = line_indent;
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
				setStage(ParseStage.CODE);

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
				setStage(ParseStage.CODE);
				new ASTIf(parent, condition);
				return true;
			}
			else
			{
				syntaxError(":", "A colon is required at the end of an if statement.");
				return false;
			}

		}

		else if (match(Syntax.KEYWORD_ELSE))
		{
			if (match(Syntax.OPERATOR_BLOCKSTART))
			{
				setStage(ParseStage.CODE);
				new ASTElse(parent);
				return true;
			}
			else
			{
				syntaxError(":", "A colon is required at the end of an else statement.");
				return false;
			}

		}

		else if (lookAhead.value.equals(Syntax.KEYWORD_LOOP))
		{
			ASTLoop loop = parseLoop(parent);
			if (loop != null)
			{
				setStage(ParseStage.CODE);
				loop.columnNumber = line_indent;
				return true;
			}
			return false;
		}

		// Try to parse as a variable declaration. //
		else if (lookAhead.tokenType == TokenType.SYMBOL
				&& nextLookAhead.value.equals(Syntax.OPERATOR_TYPESPECIFY))
		{
			ASTVariableDeclaration declaration = parseVariableDeclaration(parent);
			if (declaration != null)
			{
				setStage(ParseStage.CODE);
				declaration.columnNumber = line_indent;
				return true;
			}
			return false;
		}


		// Otherwise it's just an expression. //
		else
		{
			ASTBase expression = parseExpression(parent);
			if (expression != null)
			{
				setStage(ParseStage.CODE);
				expression.columnNumber = line_indent;
				return true;
			}
			return false;
		}
	}

	/**
	 * Parses the whole content of the Lexer
	 *
	 * It does this by calling parseLine as many times as possible.
	 */
	public void parseFile(ASTClass dest)
	{

		previous = new Token("", TokenType.UNKNOWN, 0, 1);
		// Parse as many lines as possible. //
		while (parseLine(dest));

		// Check for garbage. //
		if (!match(TokenType.EOF))
		{
			syntaxError("end of file", "There is un-parsed junk at the end of the file. ");
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
			lookAhead = nextLookAhead;
			nextLookAhead = lexer.getToken();
			return true;
		}

		return false;
	}

	private boolean match(Token.TokenType value)
	{
		if (value == lookAhead.tokenType)
		{
			previous = lookAhead;
			lookAhead = nextLookAhead;
			nextLookAhead = lexer.getToken();
			return true;
		}

		return false;
	}

	private void setStage(int newStage)
	{
		boolean success = true;
		switch (newStage)
		{
			case ParseStage.NONE:
				error("Parse Stage cannot be NONE.");
				success = false;
				break;
			case ParseStage.IMPORTS:
				if (newStage < parseStage)
				{
					unexpectedExpressionError("Import statements must take place at the start of every file.");
					success = false;
				}
				break;
			case ParseStage.FILETYPE_DECLARATION:
				if (newStage < parseStage)
				{
					unexpectedExpressionError("File declarations must take place at the start of every file, or after the import/use statements.");
					success = false;
				}
				break;
			case ParseStage.EXTENSIONS:
				if (parseStage < ParseStage.FILETYPE_DECLARATION)
				{
					unexpectedExpressionError("File type must have been declared for this expression to occur.");
					success = false;
				}
				if (newStage < parseStage)
				{
					System.out.println(newStage + ", " + parseStage);
					unexpectedExpressionError("Not expecting this expression at this stage.");
					success = false;
				}
			case ParseStage.CODE:
				if (parseStage < ParseStage.FILETYPE_DECLARATION)
				{
					unexpectedExpressionError("File type must have been declared for this expression to occur.");
					success = false;
				}
		}
		parseStage = (success) ? newStage : parseStage;
	}

	private void syntaxError(String expected, String message)
	{
		syntaxError(expected, lookAhead.value, message);
	}

	private void syntaxError(String expected, String actual, String message)
	{
		System.err.println("[Raven]: Syntax Error in file: " + lexer.fileName + "\tat line " + previous.lineNumber + ".");
		System.err.println("\tExpected:\t\t" + expected);
		System.err.println("\tActual:\t\t\t" + actual);
		System.err.println("\tMessage:\t\t" + (message.equals("") ? "[NONE]" : message));
	}

	private void unexpectedExpressionError(String message)
	{
		unexpectedExpressionError(lookAhead.value, message);
	}

	private void unexpectedExpressionError(String expression, String message)
	{
		System.err.println("[Raven]: Unexpected Expression Error in file: " + lexer.fileName + "\tat line " + previous.lineNumber + ".");
		System.err.println("Expression:\t\t" + expression);
		System.err.println("Message:\t\t" + (message.equals("") ? "[NONE]" : message));
	}

	private void error(String message)
	{
		System.err.println("[Raven]: Error in file: " + lexer.fileName + "\tat line " + previous.lineNumber + ".");
		System.err.println("Message:\t\t" + (message.equals("") ? "[NONE]" : message));
	}

}
