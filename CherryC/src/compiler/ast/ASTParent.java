package compiler.ast;

import compiler.CherryType;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;

/**
 * Defines an AST that can have child ASTs.
 *
 * @author Tyrerexus
 * @date 11/04/17
 */
public abstract class ASTParent extends ASTBase
{
	/**
	 * A list of all the ast nodes in this ast node.
	 */
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
			if (child.name.equals(symbolName) && (child instanceof ASTFunctionGroup || child instanceof ASTVariableDeclaration || child instanceof CherryType)) {
				return child;
			}
		}

		if (getParent() != null)
			return getParent().findSymbol(symbolName);

		return null;
	}

	/**
	 * Returns the last child of this parent.
	 * @return The last child of this parent.
	 */
	public ASTBase lastChild()
	{
		return childAsts.get(childAsts.size() - 1);
	}

	/**
	 * Returns true if we should compile this child.
	 * @param child The child to test against.
	 * @return Returns true if child should be compiled.
	 */
	public abstract boolean compileChild(ASTBase child);

	public void debugChildren(IndentPrinter dest)
	{
		for (ASTBase child : childAsts)
		{
			child.debugSelf(dest);
		}
	}
}
