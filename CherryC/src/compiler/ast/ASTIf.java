package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * @author Tyrerexus
 * @date 4/13/17.
 */
public class ASTIf extends ASTParent
{
	private ASTBase condition;
	ASTElse elseStatement = null;

	public ASTIf(ASTParent parent, ASTBase condition)
	{
		super(parent, "");
		this.condition = condition;
		condition.setParent(this);
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print("if (");
		condition.debugSelf(destination);
		destination.println(")");
		destination.println("{");
		destination.indentation++;
		for (ASTBase child : childAsts)
		{
			if (child != condition)
			{
				child.debugSelf(destination);
				destination.println("");
			}
		}
		destination.indentation--;

		if (elseStatement != null)
		{
			destination.println("}");
			destination.println("else");
			destination.println("{");
			destination.indentation++;
			for (ASTBase child : elseStatement.childAsts)
			{
				child.debugSelf(destination);
				destination.println("");
			}
			destination.indentation--;
		}
		destination.print("}");
	}
}
