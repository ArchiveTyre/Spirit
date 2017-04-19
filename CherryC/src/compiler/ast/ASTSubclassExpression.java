package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Created by david on 4/19/17.
 */
public class ASTSubclassExpression extends ASTBase
{
	public ASTSubclassExpression(ASTParent parent, String name)
	{
		super(parent, name);
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.println("Subclass of: " + name);
	}
}
