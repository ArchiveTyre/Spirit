package compiler;

import compiler.ast.*;
import compiler.builtins.Builtins;

/**
 * Polishes a class before compilation.
 *
 * @author Tyrerexus
 * @date 5/11/17.
 */
public class Polisher
{
	public void polishClassCreateConstructor(ASTClass astClass)
	{
		ASTVariableDeclaration v = new ASTVariableDeclaration(astClass,
				Syntax.ReservedFunctions.CONSTRUCTOR,
				Builtins.getBuiltin("function"),
				new ASTFunctionGroup(null, Syntax.ReservedFunctions.CONSTRUCTOR));
		ASTFunctionDeclaration fun = new ASTFunctionDeclaration((ASTParent)v.getValue(),
				Builtins.getBuiltin("void"));

		// There is no point in calling the super class constructor if there is no super class. //
		if (astClass.extendsClassAST != null)
		{
			// Compiles roughly to: (super.constructor) //
			new ASTFunctionCall(fun,
				new ASTMemberAccess(astClass,
						new ASTVariableUsage(fun, "super"),
						Syntax.ReservedFunctions.CONSTRUCTOR));

		}
	}

	public void polishClass(ASTClass astClass)
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
			polishClassCreateConstructor(astClass);
		}
	}
}
