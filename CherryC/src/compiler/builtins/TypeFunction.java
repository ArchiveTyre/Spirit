package compiler.builtins;

import compiler.CherryType;
import compiler.Syntax;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * @author david
 * @date 4/13/17
 */
public class TypeFunction implements CherryType
{
	@Override
	public String getTypeName()
	{
		return "function";
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return null;
	}
}
