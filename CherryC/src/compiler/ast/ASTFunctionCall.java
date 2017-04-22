package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Creates an AST node representing a function call to another node.
 *
 * Can also be used as an operator if the infix variable is set to true.
 *
 * @author alex
 * @date 4/12/17.
 */
public class ASTFunctionCall extends ASTParent
{

	private ASTVariableDeclaration declaration;
	private ASTFunctionDeclaration functionDeclaration;

	public ASTFunctionCall(ASTParent parent, String name)
	{
		super(parent, name);
		//System.out.println("Creating call with name: " + name);

		ASTBase symbol = parent.findSymbol(name);
		if (symbol instanceof  ASTVariableDeclaration)
		{
			declaration = (ASTVariableDeclaration) symbol;
			if (declaration.getValue() instanceof ASTFunctionDeclaration)
			{
				functionDeclaration = (ASTFunctionDeclaration) declaration.getValue();
			}
			else
			{
				System.err.print("Can't call non-function. ");
				functionDeclaration = null;
			}
		}
		else
		{
			System.err.println("Unknown variable desu~. " + name);
			declaration = null;
		}

	}

	@Override
	public CherryType getExpressionType()
	{
			return functionDeclaration.returnType;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{

		destination.print("Call to: " + declaration.name + "(");
		for (ASTBase arg : childAsts)
		{
			arg.debugSelf(destination);
			if (arg != childAsts.get(childAsts.size() - 1))
			{
				destination.print(", ");
			}
		}
		destination.print(")");
	}

}
