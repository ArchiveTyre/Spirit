package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTNode;

import java.util.ArrayList;

/**
 * Created by david on 4/12/17.
 */
public class TypeInteger implements CherryType
{
	@Override
	public String getTypeName()
	{
		return "int";
	}

	@Override
	public ArrayList<ASTNode> getChildNodes()
	{
		return null;
	}
}
