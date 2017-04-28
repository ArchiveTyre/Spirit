package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Created by david on 4/19/17.
 */
// FIXME: Just move to ASTClass.
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
	public void debugSelf(IndentPrinter destination)
	{
		destination.println("Subclass of: " + name);
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		// Do nothing ... //
	}
}
