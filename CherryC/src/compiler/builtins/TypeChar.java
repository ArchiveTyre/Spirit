package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTNode;

import java.util.ArrayList;

/**
 * Created by david on 4/13/17.
 */
public class TypeChar implements CherryType
{
	@Override
	public String getTypeName()
	{
		return "char";
	}

	@Override
	public ArrayList<ASTNode> getChildNodes()
	{
		return null;
	}
}
