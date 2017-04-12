package compiler.ast;

import compiler.lib.DebugPrinter;

/**
 * Creates a basic class.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTClass extends ASTParent
{

	public ASTClass(String name, ASTParent parent)
	{
		super(parent, name);
	}

	@Override
	public void debugSelf(DebugPrinter to)
	{
		to.println(name);
		to.println("{");
		to.indentation++;
		for (ASTBase child : child_asts)
		{
			child.debugSelf(to);
			to.println("");
		}
		to.indentation--;
		to.print("}");
	}
}
