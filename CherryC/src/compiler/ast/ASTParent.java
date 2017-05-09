package compiler.ast;

import compiler.CherryType;

import java.util.ArrayList;

/**
 * Defines an AST that can have child ASTs.
 *
 * @author Tyrerexus
 * @date 11/04/17`
 */
public abstract class ASTParent extends ASTNode
{
	public ArrayList<ASTNode> childAsts = new ArrayList<>();

	public ASTParent(ASTParent parent, String name)
	{
		super(parent, name);
	}

	/**
	 * Finds another AST from this AST's perspective.
	 * @param symbolName The name of the symbol we want to find.
	 * @return The symbol was found. Null if none.
	 */
	public ASTNode findSymbol(String symbolName)
	{
		// FIXME: More like findVariable!

		for (ASTNode child : childAsts) {
			if (child.name.equals(symbolName) && (child instanceof ASTVariableDeclaration || child instanceof CherryType || child instanceof ASTFunctionGroup)) {
				return child;
			}
		}

		if (getParent() != null)
			return getParent().findSymbol(symbolName);

		return null;
	}

	/**
	 * A recursive search that searches the parent/s for a symbol.
	 * @param currentParent	This node.
	 * @param symbolName	The name of the symbol to be found.
	 * @return				The found symbol, null if none found.
	 */
	public ASTNode findSymbolInParent(ASTParent currentParent, String symbolName)
	{
		for (ASTNode child : currentParent.childAsts)
		{
			if (child.name.equals(symbolName) && !child.equals(this) && (child instanceof ASTVariableDeclaration || child instanceof CherryType || child instanceof ASTFunctionGroup))
			{
				return child;
			}
		}

		if (currentParent.getParent() != null)
		{
			return findSymbolInParent(currentParent.getParent(), symbolName);
		}
		return null;
	}

	public boolean inFunction()
	{
		return inFunction(this);
	}

	private boolean inFunction(ASTParent currentParent)
	{
		if (currentParent instanceof ASTVariableDeclaration)
		{
			ASTVariableDeclaration var = (ASTVariableDeclaration) currentParent;

			if (var.childAsts.get(0) instanceof ASTFunctionDeclaration)
			{
				return true;
			}
		}
		System.out.println("STATUS: " + (currentParent.getParent() != null));


		return currentParent.getParent() != null && inFunction(currentParent.getParent());

	}





}
