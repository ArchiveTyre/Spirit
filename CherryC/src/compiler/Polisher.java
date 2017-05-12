package compiler;

import compiler.ast.*;
import compiler.builtins.Builtins;

import java.util.Iterator;

/**
 * Polishes a class before compilation.
 *
 * @author Tyrerexus
 * @date 5/11/17.
 */
public class Polisher
{
	ASTClass astClass;

	public Polisher (ASTClass astClass)
	{
		this.astClass = astClass;
	}

	public void polishClassCreateConstructor()
	{
		ASTVariableDeclaration v = new ASTVariableDeclaration(astClass,
				Syntax.ReservedFunctions.CONSTRUCTOR, Builtins.getBuiltin("function"), null);
		ASTFunctionGroup group = new ASTFunctionGroup(v, Syntax.ReservedFunctions.CONSTRUCTOR);
		ASTFunctionDeclaration fun = new ASTFunctionDeclaration(group, Builtins.getBuiltin("void"));


		// There is no point in calling the super class constructor if there is no super class. //
		/*if (astClass.extendsClassAST != null)
		{
			// Compiles roughly to: (super.constructor) //
			new ASTFunctionCall(fun,
				new ASTMemberAccess(astClass,
						new ASTVariableUsage(fun, "super"),
						Syntax.ReservedFunctions.CONSTRUCTOR));

		}
		*/
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
				if (firstCall.getDeclarationName() instanceof ASTMemberAccess)
				{
					ASTMemberAccess access = (ASTMemberAccess) firstCall.getDeclarationName();
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
		if (callsSuper == false)
		{
			// Then insert a call to the super constructor. //

			// Compiles roughly to: (super.constructor) //
			// Inserts as first thing that happens on call. //
			function.childAsts.add(0, new ASTFunctionCall(null,
					new ASTMemberAccess(astClass,
							new ASTVariableUsage(function, "super"),
							Syntax.ReservedFunctions.CONSTRUCTOR)));
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

		//Iterator<ASTBase> iterator = astClass.childAsts.iterator();
		//while (iterator.hasNext())
		for (ASTBase child : astClass.childAsts)
		{
			//iterator.
			//ASTBase child = iterator.next();
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
