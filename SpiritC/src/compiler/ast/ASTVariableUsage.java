package compiler.ast;

import compiler.SpiritType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

/**
 * @author david
 * @date 4/12/17.
 */
public class ASTVariableUsage extends ASTBase implements ASTPath
{
	/**
	 * The path to the declaration.
	 * @return Returns the declaration.
	 */
	public ASTBase getDeclaration()
	{
		if (name.equals("super"))
			return this.getContainingClass().extendsClassAST;
		else
			return getParent().findDeclaration(name);
	}

	public ASTVariableUsage(ASTChildList.ListKey key, ASTParent parent, String name)
	{
		super(key, parent, name);
	}

	@Override
	public SpiritType getExpressionType()
	{
		if (getDeclaration() == null)
		{
			System.out.println("Whoops, declaration is null!");
		}
		return getDeclaration().getExpressionType();
	}

	@Override
	public void debugSelf(IndentPrinter destination)
	{
		destination.print(name);
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileVariableUsage(this);
	}

	@Override
	public String getEnd()
	{
		return getName();
	}

	@Override
	public String toString()
	{
		return name;
	}
}
