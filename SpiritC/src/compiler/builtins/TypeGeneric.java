package compiler.builtins;

import compiler.SpiritType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * @author Tyrerexus
 * @date 02/08/17.
 */
public class TypeGeneric implements SpiritType
{
	@Override
	public String getTypeName()
	{
		return "generic";
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
		return null;
	}
}
