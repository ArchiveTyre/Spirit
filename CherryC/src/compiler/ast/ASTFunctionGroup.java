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

	public ASTFunctionGroup(ASTParent parent, String name, ASTVariableDeclaration func)
	{
		super(parent, name);
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
		destination.println("## FUNCTION GROUP ##");
		destination.indentation++;
		for (ASTVariableDeclaration var : functions)
		{
			var.debugSelf(destination);
		}
		destination.print("\n");
		destination.println("## END FUNCTION GROUP ##");
		destination.indentation--;

	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{

	}

	public boolean addFunction(ASTVariableDeclaration func)
	{
		if (func.getName().equals(functions.get(0).getName()))
		{
			functions.add(func);
			return true;
		}
		return false;
	}

	public boolean exists(ASTFunctionDeclaration testFunction)
	{
		for (ASTVariableDeclaration var : this.functions)
		{
			// Retrieve the first child, which is going to be an ASTFunctionDeclaration. //
			ASTFunctionDeclaration existingFunction = (ASTFunctionDeclaration) var.childAsts.get(0);

			// First check if the parameters are of the same length. (Otherwise they can obviously not be the same) //
			if (existingFunction.args.size() == testFunction.args.size())
			{
				// Loop through all the function parameters. //
				for (int i = 0; i < existingFunction.args.size(); i++)
				{
					ASTVariableDeclaration existingArg 	= existingFunction.args.get(i);
					ASTVariableDeclaration testArg 		= testFunction.args.get(i);

					if (existingArg.type.getTypeName().equals(testArg.type.getTypeName()))
					{
						return true;
					}
				}
			}
		}

		return false;
	}

}
