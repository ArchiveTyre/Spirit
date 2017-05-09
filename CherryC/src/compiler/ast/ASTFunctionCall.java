package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Creates an AST node representing a function call to another node.
 *
 * Can also be used as an operator if the infix variable is set to true.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTFunctionCall extends ASTParent
{



	private ASTBase declarationName;

	public ASTFunctionCall(ASTParent parent, ASTBase name)
	{
		super(parent, "");
		declarationName = name;
		declarationName.setParent(this);
	}

	@Override
	public CherryType getExpressionType()
	{
			return declarationName.getExpressionType();
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{

		String space = (childAsts.isEmpty()) ? "" : " ";
		if (getParent() instanceof ASTFunctionCall)
		{
			space = "";
		}
		declarationName.debugSelf(destination);
		destination.print("(" + space);
		for (ASTBase arg : childAsts)
		{
			arg.debugSelf(destination);
			if (arg != childAsts.get(childAsts.size() - 1))
			{
				destination.print(", ");
			}
		}
		destination.print(space + ")");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileFunctionCall(this);
	}
}
