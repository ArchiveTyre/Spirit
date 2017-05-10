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
	private ASTBase condition;
	public ASTElse elseStatement = null;

	public ASTIf(ASTParent parent, ASTBase condition)
	{
		super(parent, "");
		this.condition = condition;
		condition.setParent(this);
	}

	public ASTBase getCondition()
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
