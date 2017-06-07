package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.builtins.Builtins;
import compiler.lib.IndentPrinter;

/**
 * A string in the ast.
 *
 * @author david
 * @date 4/12/17.
 */
public class ASTString extends ASTBase
{
	/**
	 * The value of this string in string form.
	 */
	public String value;

	public ASTString(ASTParent parent, String value)
	{
		super(parent);
		this.value = value;


	}

	@Override
	public SpiritType getExpressionType()
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
