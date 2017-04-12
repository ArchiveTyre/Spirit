package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

/**
 * Created by david on 4/12/17.
 */
public class ASTVariableUsage extends ASTBase
{

	public ASTVariableDeclaration declaration;


	public ASTVariableUsage(ASTParent parent, String name)
	{
		super(parent, name);
		this.declaration = (ASTVariableDeclaration) parent.findSymbol(name);
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{

	}
}
