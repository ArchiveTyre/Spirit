package compiler.ast.builtins;

import compiler.ast.ASTBase;
import compiler.ast.ASTParent;
import compiler.lib.DebugPrinter;

/**
 * Puts a number into an AST.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTNumber extends ASTBase
{

	public int value;

	public ASTNumber(ASTParent parent, int value)
	{
		super(parent);
		this.value = value;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print(value);
	}
}
