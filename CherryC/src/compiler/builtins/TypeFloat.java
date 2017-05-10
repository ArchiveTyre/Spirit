package compiler.builtins;

import compiler.CherryType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * @author david
 * @date 4/13/17
 */
public class TypeFloat implements CherryType
{
	@Override
	public String getTypeName()
	{
		return "float";
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return null;
	}
}
