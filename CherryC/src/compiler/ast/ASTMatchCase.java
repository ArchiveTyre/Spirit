package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;


/**
 * Created by david on 5/29/17.
 */
public class ASTMatchCase extends ASTParent
{
	private boolean defaultCase;


	/**
	 * Create a default case, with no condition.
	 * @param parent The parent
	 */
	public ASTMatchCase(ASTParent parent, boolean defaultCase)
	{
		super(parent, "");
		this.defaultCase = defaultCase;

	}


	@Override
	public boolean compileChild(ASTBase child)
	{
		return false;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		ASTBase condValue = this.childAsts.get(0);
		if (defaultCase)
		{
			destination.println("default");
		}
		else
		{
			destination.print("case ");
			condValue.debugSelf(destination);
		}
		destination.println(":");

		destination.incIndent();
		for (int i = 1; i < childAsts.size(); i++)
		{
			childAsts.get(i).debugSelf(destination);
		}
		destination.decIndent();
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileCase(this);
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	public boolean isDefaultCase()
	{
		return defaultCase;
	}

}
