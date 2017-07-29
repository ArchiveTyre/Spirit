package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.builtins.Builtins;
import compiler.builtins.TypeUndefined;
import compiler.lib.Helper;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;

/**
 * A function declaration.
 * Contained in a ASTFunctionGroup that is contained in a ASTVariableDeclaration.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTFunctionDeclaration extends ASTParent
{

	/**
	 * If the function has generics, they are placed inside of this array.
	 */
	public String[] generics = null;

	/**
	 * The return type of this function.
	 */
	public SpiritType returnType;

	/**
	 * If the function is nested.
	 *
	 * Set in the constructor.
	 */
	private boolean isNestedFunction;

	private boolean anonymous = false;


	public ASTFunctionDeclaration(ASTChildList.ListKey key, ASTParent parent, SpiritType returnType)
	{
		this(key, parent, returnType, false);
	}

	public ASTFunctionDeclaration(ASTChildList.ListKey key, ASTParent parent, SpiritType returnType, boolean anonymous)
	{
		super(key, parent, "");

		children.addLists(ASTChildList.ListKey.BODY, ASTChildList.ListKey.ARGS);

		this.returnType = returnType;
		isNestedFunction = !(parent.getParent().getParent() instanceof ASTClass);
		if (isNestedFunction)
			System.out.println("Defining a nested function.");

		this.anonymous = anonymous;
	}

	@Override
	public SpiritType getExpressionType()
	{
		if (((ASTFunctionGroup)getParent()).isConstructor())
			return getContainingClass();
		else
			return returnType;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		if (isNestedFunction)
			destination.print("nested ");

		if (generics != null)
		{
			destination.print("[");
			for (String generic : generics)
			{
				destination.print(generic + " ");
			}
			destination.print("] ");
		}

		destination.print("(");
		for (ASTBase baseArg : children.getArgs())
		{
			ASTVariableDeclaration arg = (ASTVariableDeclaration) baseArg;
			String undefined = (arg.getExpressionType() instanceof TypeUndefined) ? "[?] " : "";
			destination.print(arg.name + " : " + undefined + arg.getExpressionType().getTypeName());
			if (arg != children.getLast(ASTChildList.ListKey.ARGS))
				destination.print(", ");
		}

		String undefined = (returnType instanceof TypeUndefined) ? "[?] " : "";
		destination.println(") -> " + undefined + returnType.getTypeName());
		destination.println("{");
		destination.indentation++;
		for (ASTBase child : children.getBody())
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

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileFunctionDeclaration(this);
	}

	@Override
	public ASTBase findSymbol(String symbolName)
	{
		for (ASTBase arg : children.getArgs())
		{
			if (arg.name.equals(symbolName) && (arg instanceof ASTFunctionGroup || arg instanceof ASTVariableDeclaration || arg instanceof SpiritType))
				return arg;
		}
		return super.findSymbol(symbolName);
	}
}
