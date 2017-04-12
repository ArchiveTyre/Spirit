package compiler.ast.builtins;

import compiler.ast.ASTBase;
import compiler.ast.ASTParent;
import compiler.lib.DebugPrinter;

/**
 * Created by david on 4/12/17.
 */
public class ASTString extends ASTBase
{
	public String value;

	public ASTString(ASTParent parent, String value)
	{
		super(parent);
		this.value = value;


	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{

	}
}
