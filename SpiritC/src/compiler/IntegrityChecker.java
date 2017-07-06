package compiler;

import compiler.ast.*;

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

	/**
	 * Makes sure that all calls have matching parameters to it's declaration.
	 */
	void checkFunctionCalls()
	{
		for (ASTBase ast : TraverseAST.traverse(astClass, ASTFunctionCall.class))
		{
			// Get the function group that declares all callable functions with the name of the function call. //
			ASTFunctionCall call = (ASTFunctionCall)ast;
			ASTVariableDeclaration declarationVar = (ASTVariableDeclaration) call.getDeclarationPath().getDeclaration();
			ASTFunctionGroup group;

			if (!declarationVar.isFunctionDeclaration())
			{
				// Call to variable. //
				//declarationVar = (ASTVariableDeclaration) ((ASTFunctionCall)declarationVar.getValue()).getDeclarationPath().getDeclaration();
				declarationVar = (ASTVariableDeclaration) call.getDeclarationPath().getDeclaration().getExpressionType().getChildByName(Syntax.ReservedNames.SELF);
			}

			group = (ASTFunctionGroup) declarationVar.getValue();

			// True if we found at least one matching declaration. //
			boolean hasFoundMatchingDeclaration = false;

			// Iterate and try to find a matching declaration. //
			for (ASTBase astBase : group.children.getBody())
			{
				ASTFunctionDeclaration declaration = (ASTFunctionDeclaration) astBase;

				// Here we test that the call and the declaration takes in the same amount of arguments. //
				if (declaration.children.getArgs().size() == call.children.getArgs().size())
				{
					// TODO:  We also need to check the types.
					hasFoundMatchingDeclaration = true;
				}
			}

			// If we've found an error.
			if (!hasFoundMatchingDeclaration)
			{
				System.out.println("Could not find MATCHING declaration for: " + call.getDeclarationPath().toString());
			}
		}
	}

	void checkVariableTypes()
	{

	}
}
