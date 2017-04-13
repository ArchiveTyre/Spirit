package compiler.ast;

import compiler.CherryType;
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

	public ASTFunctionDeclaration(ASTParent parent, CherryType returnType)
	{
		super(parent, "");
	}

	@Override
	public CherryType getExpressionType()
	{
		return null;
	}

	@Override
	public void debugSelf(DebugPrinter destination)
	{
		destination.print("(");
		for (ASTVariableDeclaration arg : args)
		{
			destination.print(arg.name + " : " + arg.type.getName());
			if (arg != args.get(args.size() - 1))
				destination.print(", ");
		}
		destination.println(") -> " + returnType.getName() + ": ");
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
}
