package compiler.ast;

import compiler.SpiritType;
import compiler.builtins.Builtins;

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
		// FIXME: More like findVariable! or rather, findDeclaration.

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

	/**
	 * Returns a function group by name
	 * It the function group does not exist, it will be created.
	 * @return The either created or found function group.
	 */
	public ASTFunctionGroup getFunctionGroup(String name)
	{
		ASTFunctionGroup group = null;
		for (ASTBase possibleGroup : this.children.getBody())
		{
			if (possibleGroup instanceof ASTVariableDeclaration && possibleGroup.name.equals(name))
			{
				if (((ASTVariableDeclaration) possibleGroup).isFunctionDeclaration())
				{
					group = (ASTFunctionGroup) ((ASTVariableDeclaration) possibleGroup).children.getLast(ASTChildList.ListKey.VALUE);
				}
			}
		}
		if (group == null) {
			ASTParent parent = new ASTVariableDeclaration(ASTChildList.ListKey.BODY, this, name, Builtins.getBuiltin("function"), null);
			group = new ASTFunctionGroup(ASTChildList.ListKey.VALUE, parent, name);
		}
		return group;
	}
}
