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
	 * @param symbolName
	 * @return
	 */
	public ASTBase findSymbol(String symbolName)
	{
		if (name.equals(symbolName))
			return this;

		for (ASTBase child : childAsts) {
			if (child.name.equals(symbolName) && !(child instanceof ASTVariableUsage)) {
				return child;
			}
		}

		return null;
	}



}
