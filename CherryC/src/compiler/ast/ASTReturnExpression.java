package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Created by david on 4/20/17.
 */
public class ASTReturnExpression extends ASTParent
{
	public ASTReturnExpression(ASTParent parent)
	{
		super(parent, "return");
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print("Return (");
		childAsts.get(0).debugSelf(destination);
		destination.println(")");
	}
}
