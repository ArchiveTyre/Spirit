package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Created by alex on 4/13/17.
 */
public class ASTElse extends ASTParent
{
	ASTIf ifStatement;

	public ASTElse(ASTParent parent)
	{
		super(null, "");
		ASTBase lastSibling = parent.childAsts.get(parent.childAsts.size() - 1);
		if (lastSibling instanceof ASTIf)
		{
			ifStatement = (ASTIf)lastSibling;
			if (ifStatement.elseStatement == null)
				ifStatement.elseStatement = this;
			else
				System.err.println("Can't have multiple \"else\" statements.");
		}

	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{

	}

	@Override
	public ASTParent getParent()
	{
		return ifStatement;
	}
}
