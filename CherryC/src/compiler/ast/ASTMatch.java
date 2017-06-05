package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * Created by david on 5/26/17.
 */
public class ASTMatch extends ASTParent
{

	public ASTMatch(ASTParent parent, String name)
	{
		super(parent, name);
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return false;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.println("match " + name);
		destination.incIndent();
		debugChildren(destination);
		destination.decIndent();
	}



	@Override
	public void compileSelf(LangCompiler compiler)
	{

	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}
}
