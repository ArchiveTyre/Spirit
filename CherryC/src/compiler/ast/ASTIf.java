package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * @author Tyrerexus
 * @date 4/13/17.
 */
public class ASTIf extends ASTParent
{
	private ASTNode condition;
	public ASTElse elseStatement = null;

	public ASTIf(ASTParent parent, ASTNode condition)
	{
		super(parent, "");
		this.condition = condition;
		condition.setParent(this);
	}

	public ASTNode getCondition()
	{
		return condition;
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("if (");
		condition.debugSelf(destination);
		destination.println(")");
		destination.println("{");
		destination.indentation++;
		for (ASTNode child : childAsts)
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
			for (ASTNode child : elseStatement.childAsts)
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
}
