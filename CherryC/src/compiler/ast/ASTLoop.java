package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * A simple for loop.
 *
 * @author Tyrerexus
 * @date 4/13/17.
 */
public class ASTLoop extends ASTParent
{

	public ASTBase preparationalStatement = null;
	public ASTBase initialStatement = null;
	public ASTBase conditionalStatement = null;
	public ASTBase iterationalStatement = null;

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
		if (preparationalStatement != null)
			preparationalStatement.debugSelf(destination);
		destination.println();
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
		for (ASTBase child : childAsts)
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
}
