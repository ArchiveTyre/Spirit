package compiler.builtins;

import compiler.CherryType;
import compiler.Syntax;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * Created by david on 4/13/17.
 */
public class TypeFunction implements CherryType
{
	@Override
	public String getName()
	{
		return Syntax.Type.FUNC;
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return null;
	}
}
