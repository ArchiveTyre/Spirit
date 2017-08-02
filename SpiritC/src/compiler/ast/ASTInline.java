package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.Token;
import compiler.lib.IndentPrinter;

/**
 * @author david
 * @date 5/5/17
 */
public class ASTInline extends ASTBase
{

	public String code;


	public boolean hpp = false;


	public ASTInline(ASTChildList.ListKey key, ASTParent parent, String code)
	{
		super(key, parent);
		this.code = code;

	}


	@Override
	public SpiritType getExpressionType()
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
		compiler.compileInline(this);
	}
}
