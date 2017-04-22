package compiler.ast;

import compiler.CherryType;
import compiler.builtins.Builtins;
import compiler.lib.DebugPrinter;

import java.util.ArrayList;

/**
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTFunctionDeclaration extends ASTParent
{

	public ArrayList<ASTVariableDeclaration> args = new ArrayList<>();
	public CherryType returnType;

	private boolean isNestedFunction;

	public ASTFunctionDeclaration(ASTParent parent, CherryType returnType)
	{
		super(parent, "");
		isNestedFunction = !(parent.getParent() instanceof ASTClass);
		if (isNestedFunction)
			System.out.println("Defining a nested function.");
	}

	@Override
	public CherryType getExpressionType()
	{
		return Builtins.getBuiltin("fun");
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		if (isNestedFunction)
			destination.print("nested ");
		destination.print("(");
		for (ASTVariableDeclaration arg : args)
		{
			destination.print(arg.name + " : " + arg.type.getName());
			if (arg != args.get(args.size() - 1))
				destination.print(", ");
		}
		destination.println(") -> " + returnType.getName());
		destination.println("{");
		destination.indentation++;
		for (ASTBase child : childAsts)
		{
			child.debugSelf(destination);
			destination.println("");
		}
		destination.indentation--;
		destination.print("}");
	}

	@Override
	public ASTParent getParent()
	{
		if (isNestedFunction)
			return null;

		return super.getParent();
	}
}
