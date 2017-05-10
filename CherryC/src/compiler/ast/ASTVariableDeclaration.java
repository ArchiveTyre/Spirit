package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTVariableDeclaration extends ASTParent
{
	CherryType type;

	public ASTVariableDeclaration(ASTParent parent, String name, CherryType type, ASTBase value)
	{
		super(parent, name);
		this.type = type;
		if (value != null)
			value.setParent(this);
	}

	public ASTBase getValue()
	{
		return childAsts.get(0);
	}

	@Override
	public CherryType getExpressionType()
	{
		return this.type;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("var " + type.getTypeName() + " " + name +
				" = ");
		if (childAsts.size() > 0)
			getValue().debugSelf(destination);
		else
			destination.print("undefined");

	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		if (getValue() instanceof ASTFunctionGroup)
		{
			compiler.compileFunctionGroup((ASTFunctionGroup) this.getValue());
		}
		else
		{
			compiler.compileVariableDeclaration(this);
		}
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return false;
	}
}
