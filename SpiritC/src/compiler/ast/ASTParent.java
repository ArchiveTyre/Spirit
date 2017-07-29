package compiler.ast;

import compiler.SpiritType;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Defines an AST that can have child ASTs.
 *
 * @author Tyrerexus
 * @date 11/04/17
 */
public abstract class ASTParent extends ASTBase
{

	public ASTChildList children = new ASTChildList(this);

	public ASTParent(ASTChildList.ListKey key, ASTParent parent, String name)
	{
		super(key, parent, name);
	}

	/**
	 * Finds another AST from this AST's perspective.
	 * @param symbolName The name of the symbol we want to find.
	 * @return The symbol was found. Null if none.
	 */
	public ASTBase findSymbol(String symbolName)
	{
		// FIXME: More like findVariable!

		for (ASTBase child : this.children.getAll())
		{
			if (child.name.equals(symbolName) && (child instanceof ASTFunctionGroup || child instanceof ASTVariableDeclaration || child instanceof SpiritType))
			{
				return child;
			}
		}

		if (getParent() != null)
			return getParent().findSymbol(symbolName);

		return null;
	}
}
