package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;

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
	public void debugSelf(DebugPrinter to)
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
			parent = newly_inserted_code.parent;
		}
		else
		{
			parent = newly_inserted_code.parent;
			while(parent.columnNumber >= line_indent && parent.parent != null)
				parent = parent.parent;
		}

		// Validate parent. //
		if (parent instanceof ASTParent)
		{
			return (ASTParent) parent;
		}
		else if (parent instanceof ASTVariableDeclaration)
		{
			ASTVariableDeclaration parentAsVar = (ASTVariableDeclaration)parent;
			if (parentAsVar.value instanceof ASTFunctionDeclaration)
				return (ASTFunctionDeclaration)parentAsVar.value;
		}

		// Validation failed. There is no parent. //
		return null;
	}


	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return childAsts;
	}
}
