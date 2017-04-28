package compiler.ast;

import compiler.CherryType;
import compiler.LangCompiler;
import compiler.lib.IndentPrinter;

import java.util.ArrayList;

/**
 * Creates a basic class.
 *
 * @author Tyrerexus
 * @date 4/12/17.
 */
public class ASTClass extends ASTParent implements CherryType
{
	public ASTClass(String name, ASTParent parent)
	{
		super(parent, name);
	}

	@Override
	public CherryType getExpressionType()
	{
		return this;
	}

	@Override
	public void debugSelf(IndentPrinter to)
	{
		to.println(name);
		to.println("{");
		to.indentation++;
		for (ASTBase child : childAsts)
		{
			child.debugSelf(to);
			to.println("");
		}
		to.indentation--;
		to.print("}");
	}

	public ASTParent getParentForNewCode(int line_indent)
	{


		if (childAsts.size() == 0)
			return this;

		// Get last child node. //
		ASTBase newly_inserted_code = childAsts.get(childAsts.size() - 1);

		ASTBase parent;

		// Find parent. //
		if (line_indent > newly_inserted_code.columnNumber)
		{
			parent = newly_inserted_code;
		}
		else if (line_indent == newly_inserted_code.columnNumber)
		{
			parent = newly_inserted_code.getParent();
		}
		else
		{
			parent = newly_inserted_code.getParent();
			while(parent.columnNumber >= line_indent && parent.getParent() != null)
				parent = parent.getParent();
		}

		// Validate parent. //
		if (parent instanceof ASTVariableDeclaration)
		{
			ASTVariableDeclaration parentAsVar = (ASTVariableDeclaration)parent;
			if (parentAsVar.getValue() instanceof ASTFunctionDeclaration)
				return (ASTFunctionDeclaration)parentAsVar.getValue();
		}
		else if (parent instanceof ASTParent)
		{
			return (ASTParent) parent;
		}

		// Validation failed. There is no parent. //
		return null;
	}


	@Override
	public String getTypeName()
	{
		return name;
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return childAsts;
	}

	@Override
	public void compileSelf(LangCompiler compiler)
	{
		compiler.compileClass(this);
	}
}
