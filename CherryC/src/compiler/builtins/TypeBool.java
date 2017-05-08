package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTNode;

import java.util.ArrayList;

/**
 * Created by david on 4/13/17.
 */
public class TypeBool implements CherryType
{

	@Override
	public String getTypeName()
	{
		return "bool";
	}

	@Override
	public ArrayList<ASTNode> getChildNodes()
	{
		return null;
	}
}
