package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * A simple for loop.
 *
 * @author Tyrerexus
 * @date 4/13/17.
 */
public class ASTLoop extends ASTParent
{

	public ASTNode preparationalStatement = null;
	public ASTNode initialStatement = null;
	public ASTNode conditionalStatement = null;
	public ASTNode iterationalStatement = null;

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
	public void debugSelf(IndentPrinter destination)
	{
		if (preparationalStatement != null)
		{
			preparationalStatement.debugSelf(destination);
			destination.println();
		}
		destination.print("for (");
		if (initialStatement != null)
			initialStatement.debugSelf(destination);
		destination.print("; ");
		if (conditionalStatement != null)
			conditionalStatement.debugSelf(destination);
		destination.print("; ");
		if (iterationalStatement != null)
			iterationalStatement.debugSelf(destination);
		destination.println(")");
		destination.println("{");
		destination.indentation++;
		for (ASTNode child : childAsts)
		{
			if (child != preparationalStatement
					&& child != initialStatement
					&& child != conditionalStatement
					&& child != iterationalStatement)
			{
				child.debugSelf(destination);
				destination.println("");
			}
		}
		destination.indentation--;
		destination.print("}");

	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileLoop(this);
	}
}
