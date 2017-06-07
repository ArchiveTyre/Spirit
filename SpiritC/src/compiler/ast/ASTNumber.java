package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.builtins.Builtins;
import compiler.lib.IndentPrinter;

/**
 * Puts a number into an AST.
 *
 * TODO: Support more than just integers in size.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTNumber extends ASTBase
{

	/**
	 * The value of this number.
	 */
	public int value;

	public ASTNumber(ASTParent parent, int value)
	{
		super(parent);
		this.value = value;
	}

	@Override
	public SpiritType getExpressionType()
	{
		return Builtins.getBuiltin("int");
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print(value);
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileNumber(this);
	}
}
