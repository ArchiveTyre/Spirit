package compiler.ast;

import compiler.SpiritType;
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

	public ASTBase preparationalStatement = null;
	public ASTBase initialStatement = null;
	public ASTBase conditionalStatement = null;
	public ASTBase iterationalStatement = null;

	public ASTLoop(ASTChildList.ListKey key, ASTParent parent)
	{
		super(key, parent, "");

		children.addLists(ASTChildList.ListKey.BODY,
						  ASTChildList.ListKey.FOR_CONDITION,
						  ASTChildList.ListKey.FOR_INIT,
						  ASTChildList.ListKey.FOR_ITERATIONAL);
	}

	@Override
	public SpiritType getExpressionType()
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
		for (ASTBase child : children.getBody())
		{
			if (compileChild(child))
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

	@Override
	public boolean compileChild(ASTBase child)
	{
		return child != preparationalStatement
				&& child != initialStatement
				&& child != conditionalStatement
				&& child != iterationalStatement;
	}
}
