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

	public ASTVariableDeclaration(ASTParent parent, String name, CherryType type, ASTNode value)
	{
		super(parent, name);
		this.type = type;
		if (value != null)
			value.setParent(this);
	}

	public ASTNode getValue()
	{
		return childAsts.get(0);
	}

	@Override
	public CherryType getExpressionType()
	{
		if (childAsts.get(0) instanceof ASTFunctionDeclaration)
		{
			return ((ASTFunctionDeclaration) childAsts.get(0)).returnType;
		}
		else
		{
			return this.type;
		}
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
		if (childAsts.get(0) instanceof ASTFunctionDeclaration)
		{
			compiler.compileFunctionDeclaration(this);
		}
		else
		{
			compiler.compileVariableDeclaration(this);
		}
	}
}
