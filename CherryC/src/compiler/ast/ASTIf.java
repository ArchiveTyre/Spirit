package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Created by alex on 4/13/17.
 */
public class ASTIf extends ASTParent
{
	public ASTBase condition;
	public ASTElse elseStatement = null;

	public ASTIf(ASTParent parent, ASTBase condition)
	{
		super(parent, "");
		this.condition = condition;
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print("if ");
		condition.debugSelf(destination);
		destination.println("{");
		destination.indentation++;
		for (ASTBase child : childAsts)
		{
			child.debugSelf(destination);
			destination.println("");
		}
		destination.indentation--;

		if (elseStatement != null)
		{
			destination.print("}");
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
