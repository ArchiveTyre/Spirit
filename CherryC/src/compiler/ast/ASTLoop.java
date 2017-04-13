package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 *
 *
 * @author Tyrerexus
 * @date 4/13/17.
 */
public class ASTLoop extends ASTParent
{

	public ASTLoop(ASTParent parent)
	{
		super(parent, "");
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{

	}
}
