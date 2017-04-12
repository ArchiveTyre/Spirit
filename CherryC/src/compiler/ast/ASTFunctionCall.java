package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Created by alex on 4/12/17.
 */
public class ASTFunctionCall extends ASTParent
{

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
		if (infix)
		{
			childAsts.get(0).debugSelf(destination);
			destination.print(" " + name + " ");
			childAsts.get(1).debugSelf(destination);
		}
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
