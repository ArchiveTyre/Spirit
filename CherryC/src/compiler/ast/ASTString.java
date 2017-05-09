package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.builtins.Builtins;
import compiler.lib.IndentPrinter;

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
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("\"" + value + "\"");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileString(this);
	}
}
