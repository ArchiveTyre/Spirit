package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * @author david
 * @date 4/13/17
 */
public class TypeLong implements CherryType
{

	@Override
	public String getTypeName()
	{
		return "long";
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return null;
	}
}
