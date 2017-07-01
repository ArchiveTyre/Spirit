package compiler.ast;

import compiler.SpiritType;
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
	/**
	 * This ASTIf that this ASTElse is connected with.
	 */
	private ASTIf ifStatement;

	public ASTElse(ASTParent parent)
	{
		super(ASTChildList.ListKey.BODY, parent, "");

		children.addLists(ASTChildList.ListKey.BODY);

		// Try to find the owning ASTIf. //
		// Find the previous last child in body and check if ASTIf. //
		ASTBase lastSibling = parent.children.getBody().get(parent.children.getBody().size() - 2);
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
	public SpiritType getExpressionType()
	{
		return null;
	}

	@Override
	public ASTParent getParent()
	{
		return ifStatement;
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return true;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		/*
		 * This does nothing because it's printed out by the owning ASTIf.
		 */
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		/*
		 * This does nothing because it's compiled by the owning ASTIf
		 */
	}
}
