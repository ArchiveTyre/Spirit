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
		super(parent, "");
		ASTBase lastSibling = parent.childAsts.get(parent.childAsts.size() - 2);
		if (lastSibling != null && lastSibling instanceof ASTIf)
		{
			ifStatement = (ASTIf)lastSibling;
			if (ifStatement.elseStatement == null)
				ifStatement.elseStatement = this;
			else
				System.err.println("Can't have multiple \"else\" statements.");
		}
		else
		{
			System.err.println("Could not find owning \"if\" statement. ");
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
		/*
		 * This does nothing because it's printed out by the owning "if" statement.
		 */
	}

	@Override
	public ASTParent getParent()
	{
		return ifStatement;
	}
}
