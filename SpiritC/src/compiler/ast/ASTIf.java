package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Defines a simple conditional statement, "if".
 * Can also have an attached "elseStatement".
 *
 * @author Tyrerexus
 * @date 4/13/17.
 */
public class ASTIf extends ASTParent
{

	/**
	 * Optionally attached "else" statement.
	 */
	public ASTElse elseStatement = null;

	/**
	 * Getter for condition to check against.
	 * @return The condition to check against.
	 */
	public ASTBase getCondition()
	{
		return children.getLast(ASTChildList.ListKey.CONDITION);
	}

	public ASTIf(ASTChildList.ListKey key, ASTParent parent)
	{
		super(key, parent, "");

		children.addLists(ASTChildList.ListKey.BODY, ASTChildList.ListKey.CONDITION);
	}

	@Override
	public SpiritType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("if (");
		getCondition().debugSelf(destination);
		destination.println(")");
		destination.println("{");
		destination.indentation++;
		for (ASTBase child : children.getBody())
		{
			child.debugSelf(destination);
			destination.println("");
		}
		destination.indentation--;

		if (elseStatement != null)
		{
			destination.println("}");
			destination.println("else");
			destination.println("{");
			destination.indentation++;
			for (ASTBase child : elseStatement.children.getBody())
			{
				child.debugSelf(destination);
				destination.println("");
			}
			destination.indentation--;
		}
		destination.print("}");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileIf(this);
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return false;
	}
}
