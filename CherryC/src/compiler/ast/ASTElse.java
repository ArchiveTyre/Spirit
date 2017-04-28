package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Defines a block of code to be run at an ASTIf's failure.
 * Note that ASTElse can not exist in the AST without an owning ASTIf.
 *
 * @author Tyrerexus
 * @date 4/13/17.
 */
public class ASTElse extends ASTParent
{
	private ASTIf ifStatement;

	public ASTElse(ASTParent parent)
	{
		super(parent, "");

		// Try to find the owning ASTIf. //
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
	public void debugSelf(IndentPrinter destination)
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

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		// Do nothing. //
	}
}
