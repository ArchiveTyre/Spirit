package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTNode;

import java.util.ArrayList;

/**
 * @author david
 * @date 4/13/17.
 */
public class TypeFunction implements CherryType
{
	@Override
	public String getTypeName()
	{
		return "function";
	}

	@Override
	public ArrayList<ASTNode> getChildNodes()
	{
		return null;
	}
}
