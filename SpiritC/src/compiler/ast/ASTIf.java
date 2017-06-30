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
	 * The condition to check against.
	 */
	private ASTBase condition;

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
		return condition;
	}

	public ASTIf(ASTParent parent, ASTBase condition)
	{
		super(parent, "");
		this.condition = condition;
		condition.setParent(this);
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
