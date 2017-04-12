package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTFunctionDeclaration extends ASTBase
{
	public ASTFunctionDeclaration(ASTParent parent, String name)
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

	}
}
