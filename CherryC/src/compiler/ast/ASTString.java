package compiler.ast;

import compiler.CherryType;
import compiler.ast.ASTBase;
import compiler.ast.ASTParent;
import compiler.builtins.Builtins;
import compiler.lib.DebugPrinter;

/**
 * @author david
 * @date 4/12/17.
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
	public CherryType getExpressionType()
	{
		return Builtins.getBuiltin("string");
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{

	}
}
