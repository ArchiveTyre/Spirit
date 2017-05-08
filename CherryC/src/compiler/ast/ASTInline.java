package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Created by david on 5/5/17.
 */
public class ASTInline extends ASTNode
{

	String code;
	public ASTInline(ASTParent parent, String code)
	{
		super(parent);
		this.code = code;

	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.println("#backend");
		destination.println(code);
		destination.println("#end-backend");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
	}
}
