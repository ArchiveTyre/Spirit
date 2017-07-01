package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.builtins.Builtins;
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
	 * The arguments that are needed to call this function.
	 */
	public ArrayList<ASTBase> args = new ArrayList<>();


	/**
	 * The ASTs that are contained within the body of the function.
	 */
	public ArrayList<ASTBase> body = new ArrayList<>();



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
		return Builtins.getBuiltin("function");
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		if (isNestedFunction)
			destination.print("nested ");
		destination.print("(");
		for (ASTBase baseArg : args)
		{
			ASTVariableDeclaration arg = (ASTVariableDeclaration) baseArg;
			destination.print(arg.name + " : " + arg.getExpressionType().getTypeName());
			if (arg != args.get(args.size() - 1))
				destination.print(", ");
		}
		destination.println(") -> " + returnType.getTypeName());
		destination.println("{");
		destination.indentation++;
		for (ASTBase child : body)
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
		for (ASTBase arg : args)
		{
			if (arg.name.equals(symbolName) && (arg instanceof ASTFunctionGroup || arg instanceof ASTVariableDeclaration || arg instanceof SpiritType))
				return arg;
		}
		return super.findSymbol(symbolName);
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return true;
	}
}
