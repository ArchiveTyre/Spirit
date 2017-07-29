package compiler.ast;

import compiler.SpiritType;
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
	public ASTReturnExpression(ASTChildList.ListKey key, ASTParent parent)
	{
		super(key, parent, "return");

		children.addLists(ASTChildList.ListKey.VALUE);
	}

	@Override
	public SpiritType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("Return (");
		children.getValue().get(0).debugSelf(destination);
		destination.println(")");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileReturnExpression(this);
	}
}
