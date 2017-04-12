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

	@Override
	public ASTBase findSymbol(String symbolName)
	{
		ASTBase result = super.findSymbol(symbolName);
		if (result != null)
			return result;

		for (ASTBase child : childAsts) {
			if (child.name == symbolName) {
				return child;
			}
		}

		return null;
	}



}
