package compiler.ast;

import compiler.SpiritCallable;
import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * A declaration of a variable.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTVariableDeclaration extends ASTParent implements SpiritCallable
{
	/**
	 * The type of this variable declaration.
	 */
	public SpiritType type;

	/**
	 * Gets the initial value of this declaration.
	 * @return The initial value of this declaration.
	 */
	public ASTBase getValue()
	{
		return children.getValue().size() > 0 ? children.getValue().get(0) : null;
	}

	public ASTVariableDeclaration(ASTChildList.ListKey key, ASTParent parent, String name, SpiritType type, ASTBase value)
	{
		super(key, parent, name);

		children.addLists(ASTChildList.ListKey.VALUE);

		this.type = type;
		if (name.equals("other"))
		{
			int a = 1;
		}
		if (value != null)
			value.setParent(key, this);
	}

	/**
	 * Checks if this variable declaration is actually a function declaration.
	 * @return True if the value is a ASTFunctionGroup.
	 */
	public boolean isFunctionDeclaration()
	{
		return getValue() instanceof ASTFunctionGroup;
	}

	@Override
	public SpiritType getExpressionType()
	{
		return this.type;
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print("var " + type.getTypeName() + " " + name +
				" = ");
		if (getValue() != null)
			getValue().debugSelf(destination);
		else
			destination.print("undefined");

	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		if (getValue() instanceof ASTFunctionGroup)
		{
			compiler.compileFunctionGroup((ASTFunctionGroup) this.getValue());
		}
		else
		{
			compiler.compileVariableDeclaration(this);
		}
	}
}
