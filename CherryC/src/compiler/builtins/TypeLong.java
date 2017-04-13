package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * Created by david on 4/13/17.
 */
public class TypeLong implements CherryType
{

	@Override
	public String getName()
	{
		return "long";
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return null;
	}
}
