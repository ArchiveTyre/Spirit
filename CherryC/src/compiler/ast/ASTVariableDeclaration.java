package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTVariableDeclaration extends ASTBase
{
	CherryType type;
	ASTBase value;

	public ASTVariableDeclaration(ASTParent parent, String name, CherryType type, ASTBase value)
	{
		super(parent, name);
		this.type = type;
		this.value = value;
		if (value != null)
			value.setParent(null);
	}


	@Override
	public CherryType getExpressionType()
	{
		return this.type;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print("Variable: " + name + " of type: " + type.getName() + " with value of ");
		value.debugSelf(destination);

	}
}
