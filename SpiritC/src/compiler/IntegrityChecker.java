package compiler;

import compiler.ast.ASTBase;
import compiler.ast.ASTClass;
import compiler.ast.ASTFunctionCall;
import compiler.ast.TraverseAST;

/**
 * Created by alex on 30/06/17.
 */
public class IntegrityChecker
{
	ASTClass astClass;

	public IntegrityChecker(ASTClass astClass)
	{
		this.astClass = astClass;
	}

	void checkIntegrity()
	{
		checkFunctionCalls();
		checkVariableTypes();
	}

	void checkFunctionCalls()
	{
		for (ASTBase ast : TraverseAST.traverse(astClass, ASTFunctionCall.class))
		{
			ASTFunctionCall call = (ASTFunctionCall)ast;
			System.out.println("Found: " + call.getDeclarationPath().getName());
		}
	}

	void checkVariableTypes()
	{

	}
}
