package compiler;

import compiler.ast.*;

/**
 * This class can check if a class has type integrity.
 *
 * @author Tyrerexus
 * @date 30/06/17
 */
public class IntegrityChecker
{
	ASTClass astClass;

	public IntegrityChecker(ASTClass astClass)
	{
		this.astClass = astClass;
	}

	/**
	 * Checks all possible integrities of a class.
	 */
	void checkIntegrity()
	{
		checkFunctionCalls();
		checkVariableTypes();
		checkAssignments();
	}

	/**
	 * Checks that L side is assignable from R side of assignment operators.
	 */
	private void checkAssignments()
	{
		for (ASTBase ast : TraverseAST.traverse(astClass, ASTOperator.class))
		{
			ASTOperator operator = (ASTOperator) ast;

			if (operator.getName().equals(Syntax.Op.Assign.IS))
			{
				if (operator.getLeftExpression().getExpressionType() != operator.getRightExpression().getExpressionType())
				{
					System.err.println("ERROR: Type miss-match in assignment of: " + operator.getLeftExpression().toString());
				}
			}
		}
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
				System.err.println("ERROR: Could not find MATCHING declaration for: " + call.getDeclarationPath().toString());
			}
		}
	}

	void checkVariableTypes()
	{
		for (ASTBase ast : TraverseAST.traverse(astClass, ASTVariableDeclaration.class))
		{
			ASTVariableDeclaration declaration = (ASTVariableDeclaration) ast;

			if (declaration.getValue() != null && !declaration.isFunctionDeclaration())
			{
				if (declaration.getExpressionType() != declaration.getValue().getExpressionType())
				{
					System.err.println("ERROR: Type miss-match in declaration!: " + declaration.getName());
				}
			}
		}
	}
}
