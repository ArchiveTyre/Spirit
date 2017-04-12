package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * @author david
 * @date 4/12/17.
 */
public class ASTVariableUsage extends ASTBase
{

	public ASTVariableDeclaration declaration;


	public ASTVariableUsage(ASTParent parent, String name)
	{
		super(null, name);
		this.declaration = (ASTVariableDeclaration) parent.findSymbol(name);
		setParent(parent);
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print(name);
	}
}
