package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTVariable extends ASTBase
{
	CherryType type;
	ASTBase value;

	public ASTVariable(ASTParent parent, String name, CherryType type, ASTBase value)
	{
		super(parent, name);
		this.type = type;
		this.value = value;
		if (value != null)
			value.setParent(null);
	}


	@Override
	public void debugSelf(DebugPrinter destination)
	{

	}
}
