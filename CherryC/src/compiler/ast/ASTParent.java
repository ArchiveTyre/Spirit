package compiler.ast;

import java.util.ArrayList;

/**
 * Defines an AST that can have child ASTs.
 *
 * @author Tyrerexus
 * @date 11/04/17
 */
public abstract class ASTParent extends ASTBase
{
	public ArrayList<ASTBase> childAsts = new ArrayList<>();

	public ASTParent(ASTParent parent, String name)
	{
		super(parent, name);
	}

	/**
	 * Finds another AST from this AST's perspective.
	 * @param symbolName The name of the symbol we want to find.
	 * @return The symbol was found. Null if none.
	 */
	public ASTBase findSymbol(String symbolName)
	{
		// FIXME: More like findVariable!

		for (ASTBase child : childAsts) {
			if (child.name.equals(symbolName) && child instanceof ASTVariableDeclaration) {
				return child;
			}
		}

		if (getParent() != null)
			return getParent().findSymbol(symbolName);

		return null;
	}



}
