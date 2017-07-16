package compiler.builtins;

import compiler.SpiritType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * @author david
 * @date 4/13/17
 */
public class TypeShort implements SpiritType
{
	@Override
	public String getTypeName()
	{
		return "short";
	}

	@Override
	public ArrayList<ASTBase> getChildNodes()
	{
		return null;
	}

	@Override
	public ASTBase getChildByName(String name)
	{
		return null;
	}

	@Override
	public SpiritType getSuperType()
	{
		return Builtins.getBuiltin("rational_number");
	}
}
