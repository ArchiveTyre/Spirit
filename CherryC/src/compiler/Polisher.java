package compiler;

import compiler.ast.*;
import compiler.builtins.Builtins;

/**
 * Polishes a class before compilation.
 *
 * @author Tyrerexus
 * @date 5/11/17.
 */
@SuppressWarnings("WeakerAccess")
public class Polisher
{
	private ASTClass astClass;

	public Polisher (ASTClass astClass)
	{
		this.astClass = astClass;
	}

	public void polishClassCreateConstructor()
	{
		ASTVariableDeclaration v = new ASTVariableDeclaration(astClass,
				Syntax.ReservedFunctions.CONSTRUCTOR, Builtins.getBuiltin("function"), null);
		ASTFunctionGroup group = new ASTFunctionGroup(v, Syntax.ReservedFunctions.CONSTRUCTOR);
		new ASTFunctionDeclaration(group, Builtins.getBuiltin("void"));
	}

	/**
	 * Makes sure that the constructor calls the super constructor.
	 * @param function The constructor to check.
	 */
	public void polishClassConstructor(ASTFunctionDeclaration function)
	{
		boolean callsSuper = false;

		// We can't check something that doesn't have anything. //
		if (function.childAsts.size() > 0)
		{
			ASTBase firstThing = function.childAsts.get(0);
			if (firstThing instanceof ASTFunctionCall)
			{
				ASTFunctionCall firstCall = (ASTFunctionCall) firstThing;

				// Check if the first call calls "super.init". //
				if (firstCall.getDeclarationPath() instanceof ASTMemberAccess)
				{
					ASTMemberAccess access = (ASTMemberAccess) firstCall.getDeclarationPath();
					if (access.getMemberName().equals(Syntax.ReservedFunctions.CONSTRUCTOR))
					{
						if (access.ofObject.getName().equals("super"))
						{
							callsSuper = true;
						}
					}
				}
			}
		}

		// If the first thing in the constructor isn't a call to the super constructor. //
		if (!callsSuper)
		{
			// Then insert a call to the super constructor. //

			// Compiles roughly to: (super.constructor) //
			// Inserts as first thing that happens on call. //
			ASTFunctionCall astFunctionCall = new ASTFunctionCall(null);
			astFunctionCall.setDeclarationPath(
					new ASTMemberAccess(astClass,
							new ASTVariableUsage(astFunctionCall, "super"),
					Syntax.ReservedFunctions.CONSTRUCTOR));
			function.childAsts.add(0, astFunctionCall);
		}
	}

	/**
	 * This function checks if each constructor in astClass calls
	 * The super constructor, and if not: adds a call to the super constructor.
	 */
	public void forceConstructorsCallSuper()
	{

		// We can't force constructors to call a super constructor that does not exist. //
		if (astClass.extendsClassAST == null)
			return;

		for (ASTBase child : astClass.childAsts)
		{
			if (child instanceof ASTVariableDeclaration
					&& ((ASTVariableDeclaration)child).isFunctionDeclaration())
			{
				ASTFunctionGroup group = (ASTFunctionGroup)((ASTVariableDeclaration) child).getValue();
				if (group.isConstructor())
				{
					for (ASTBase possibleFunction : group.childAsts)
					{
						polishClassConstructor((ASTFunctionDeclaration) possibleFunction);
					}
				}
			}
		}
	}

	public void polishClass()
	{

		// There is no need to polish the finest object... (#^.^#)//
		if (astClass.getName().equals("object"))
			return;

		// Make sure that the class extends something. //
		if (astClass.extendsClassAST == null)
		{
			astClass.extendClass("object");
		}


		// Make sure that there is at least one constructor! //
		if (!astClass.getConstructorDeclared())
		{
			polishClassCreateConstructor();
		}

		forceConstructorsCallSuper();
	}
}
