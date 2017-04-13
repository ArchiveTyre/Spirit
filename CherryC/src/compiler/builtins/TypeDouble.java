package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * Created by david on 4/13/17.
 */
public class TypeDouble implements CherryType
{
	@Override
	public String getName()
	{
		return "double";
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return null;
	}
}
