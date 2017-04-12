package compiler.ast;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Defines an AST that can have child ASTs.
 *
 * @author Tyrerexus
 * @date 11/04/17
 */
public abstract class ASTParent extends ASTBase
{
	ArrayList<ASTBase> child_asts = new ArrayList<>();

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

		for (ASTBase child : child_asts) {
			if (child.name == symbolName) {
				return child;
			}
		}

		return null;
	}
}
