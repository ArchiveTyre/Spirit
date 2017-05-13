package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Defines a return statement for a function.
 *
 * @author david
 * @date 4/20/17
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
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("Return (");
		childAsts.get(0).debugSelf(destination);
		destination.println(")");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileReturnExpression(this);
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return false;
	}
}
