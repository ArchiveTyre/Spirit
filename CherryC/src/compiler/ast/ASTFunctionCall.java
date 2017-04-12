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

	/**
	 * Used mainly to create operators.
	 * If set to true the function name will be in the middle of the arguments.
	 */
	public boolean infix = false;

	public ASTFunctionCall(ASTParent parent, String name)
	{
		super(parent, name);
		// FIXME: Find declaration.
	}

	@Override
	public CherryType getExpressionType()
	{
		if (infix)
		{

			// FIXME: This is ugly. We should perhaps give operators their own class?
			return childAsts.get(0).getExpressionType();
		}
		else
		{
			System.err.println("ERROR: Not implemented yet.");
			return null;
		}
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{

		// When infix the arguments are around the function name. //
		if (infix)
		{
			destination.print("(");
			childAsts.get(0).debugSelf(destination);
			destination.print(" " + name + " ");
			childAsts.get(1).debugSelf(destination);
			destination.print(")");
		}

		// Just a normal function call. //
		else
		{
			destination.print("Call to: " + name + "(");
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
}
