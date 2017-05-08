package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;

/**
 * This classes purpose is to serve
 * as a group for overloaded functions.
 */
public class ASTFunctionGroup extends ASTNode
{

	public ArrayList<ASTVariableDeclaration> functions = new ArrayList<>();

	public ASTFunctionGroup(ASTParent parent, ASTVariableDeclaration func)
	{
		super(parent);
		functions.add(func);
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{

	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{

	}

	public boolean  addFunction(ASTVariableDeclaration func)
	{
		if (func.getName() == functions.get(0).getName())
		{
			functions.add(func);
			return true;
		}
		return false;
	}

}
