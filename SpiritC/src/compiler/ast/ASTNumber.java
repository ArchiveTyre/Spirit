package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.builtins.Builtins;
import compiler.lib.IndentPrinter;

import java.math.BigDecimal;

/**
 * Puts a number into an AST.
 *
 * TODO: Support decimal numbers in the constructor.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTNumber extends ASTBase
{

	/**
	 * The value of this number.
	 */
	public BigDecimal value;

	public ASTNumber(ASTChildList.ListKey key, ASTParent parent, int value)
	{
		super(key, parent);
		this.value = new BigDecimal(value);
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
