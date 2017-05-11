package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.Syntax;
import compiler.lib.IndentPrinter;

/**
 * @author Tyrerexus
 * @date 5/9/17.
 */
public class ASTFunctionGroup extends ASTParent
{
	public ASTFunctionGroup(ASTParent parent, String name)
	{
		super(parent, name);
	}

	public boolean isConstructor()
	{
		return name.equals(Syntax.ReservedFunctions.CONSTRUCTOR);
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return true;
	}

	@Override
	public CherryType getExpressionType()
	{
		return childAsts.get(0).getExpressionType();
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.println("FunctionGroup");
		destination.println("{");
		destination.indentation++;
		for(ASTBase node : childAsts)
		{
			node.debugSelf(destination);
			destination.println();
		}
		destination.indentation--;
		destination.println("}");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileFunctionGroup(this);
	}
}
