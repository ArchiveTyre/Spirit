package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.Syntax;
import compiler.lib.IndentPrinter;

/**
 * Creates an AST node representing a function call to another node.
 *
 * Can also be used as an operator if the infix variable is set to true.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTFunctionCall extends ASTParent
{
	/**
	 * This is either a ASTMemberAccess or a ASTVariable.
	 * It shows us what to call.
	 */
	private ASTPath declarationPath;

	public ASTPath getDeclarationPath()
	{
		return declarationPath;
	}

	public void setDeclarationPath(ASTPath newDeclarationPath)
	{
		declarationPath = newDeclarationPath;
		newDeclarationPath.setParent(ASTChildList.ListKey.PATH, this);
	}

	/**
	 * Checks if this is a call to a constructor.
	 * @return True if this is a call to a constructor.
	 */
	public boolean isConstructorCall()
	{
		// TODO: Put in Syntax class.
		return declarationPath.getEnd().equals(Syntax.ReservedNames.CONSTRUCTOR);
	}

	public ASTFunctionCall(ASTChildList.ListKey key, ASTParent parent)
	{
		super(key, parent, "");
		children.addLists(ASTChildList.ListKey.ARGS, ASTChildList.ListKey.PATH);
	}

	@Override
	public SpiritType getExpressionType()
	{
			return declarationPath.getExpressionType();
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		String space = children.getArgs().isEmpty() || getParent() instanceof ASTFunctionCall
				? ""
				: " ";
		declarationPath.debugSelf(destination);
		destination.print("(" + space);
		for (ASTBase arg : children.getArgs())
		{
			if (compileChild(arg))
			{
				arg.debugSelf(destination);
				if (arg != children.getArgs().get(children.getArgs().size() - 1))
				{
					destination.print(", ");
				}
			}
		}
		destination.print(space + ")");
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileFunctionCall(this);
	}

	@Override
	public boolean compileChild(ASTBase child)
	{
		return !(child == declarationPath);
	}
}
