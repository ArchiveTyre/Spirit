package compiler;

import compiler.ast.*;
import compiler.ast.ASTChildList.ListKey;
import compiler.builtins.Builtins;

import java.util.ArrayList;
import java.util.List;

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

	public void forceExtendSomething()
	{
		if (astClass.extendsClassAST == null)
			astClass.extendClass(Syntax.ReservedNames.OBJECT_CLASS);
	}

	public void polishClassCreateConstructor()
	{
		ASTVariableDeclaration v = new ASTVariableDeclaration(ListKey.BODY, astClass,
				Syntax.ReservedNames.CONSTRUCTOR, Builtins.getBuiltin("function"), null);
		ASTFunctionGroup group = new ASTFunctionGroup(ListKey.BODY ,v, Syntax.ReservedNames.CONSTRUCTOR);
		new ASTFunctionDeclaration(ListKey.BODY, group, Builtins.getBuiltin("void"));
	}

	/**
	 * Makes sure that the constructor calls the super constructor.
	 * @param function The constructor to check.
	 */
	public void polishClassConstructor(ASTFunctionDeclaration function)
	{
		boolean callsSuper = false;

		// We can't check something that doesn't have anything. //
		if (function.children.getBody().size() > 0)
		{
			ASTBase firstThing = function.children.getFirst();
			if (firstThing instanceof ASTFunctionCall)
			{
				ASTFunctionCall firstCall = (ASTFunctionCall) firstThing;

				// Check if the first call calls "super.new". //
				if (firstCall.getDeclarationPath() instanceof ASTMemberAccess)
				{
					ASTMemberAccess access = (ASTMemberAccess) firstCall.getDeclarationPath();
					if (access.getEnd().equals(Syntax.ReservedNames.CONSTRUCTOR)
							&& access.ofObject.getName().equals("super"))
					{
						callsSuper = true;
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
			// Thus, parent is specified later. //
			ASTFunctionCall astFunctionCall = new ASTFunctionCall(ListKey.BODY, function);
			astFunctionCall.setDeclarationPath(
					new ASTMemberAccess(ListKey.BODY, astClass,
							new ASTVariableUsage(ListKey.BODY, astFunctionCall, "super"),
					Syntax.ReservedNames.CONSTRUCTOR));
			function.body.remove(astFunctionCall);
			function.body.add(0, astFunctionCall);
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




		for (ASTBase child : astClass.children.getAll())
		{
			if (child instanceof ASTVariableDeclaration
					&& ((ASTVariableDeclaration)child).isFunctionDeclaration())
			{
				ASTFunctionGroup group = (ASTFunctionGroup)((ASTVariableDeclaration) child).getValue();
				if (group.isConstructor())
				{
					for (ASTBase possibleFunction : group.children.getBody())
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
		if (astClass.getName().equals(Syntax.ReservedNames.OBJECT_CLASS))
			return;

		// Make sure that the class extends something. //
		forceExtendSomething();


		// Make sure that there is at least one constructor! //
		if (!astClass.getConstructorDeclared())
		{
			polishClassCreateConstructor();
		}

		forceConstructorsCallSuper();
	}
}
