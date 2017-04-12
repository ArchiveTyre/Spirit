package compiler.ast;

import compiler.CherryType;
import compiler.lib.DebugPrinter;
import org.junit.jupiter.api.Test;

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
	public void debugSelf(DebugPrinter to)
	{
		to.println(name);
		to.println("{");
		to.indentation++;
		for (ASTBase child : child_asts)
		{
			child.debugSelf(to);
			to.println("");
		}
		to.indentation--;
		to.print("}");
	}

	public ASTParent getParentForNewCode(int line_indent)
	{

		if (child_asts.size() == 0)
			return this;

		// Get last child node. //
		ASTBase newly_inserted_code = child_asts.get(child_asts.size() - 1);

		if (line_indent > newly_inserted_code.columnNumber)
		{
			if (newly_inserted_code instanceof ASTParent)
			{
				return (ASTParent)newly_inserted_code;
			}
			return null;
		}
		else if (line_indent == newly_inserted_code.columnNumber)
		{
			if (newly_inserted_code.parent instanceof ASTParent)
			{
				return newly_inserted_code.parent;
			}
			return null;
		}
		else
		{
			ASTBase parent = newly_inserted_code.parent;
			while(parent.columnNumber >= line_indent && parent.parent != null)
				parent = parent.parent;

			if (parent instanceof ASTParent)
			{
				return (ASTParent) parent;
			}

			return null;
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return child_asts;
	}
}
