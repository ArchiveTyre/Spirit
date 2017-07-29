package compiler.builtins;

import compiler.SpiritType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * @author Tyrerexus
 * @date 7/16/17
 */
public class TypeRationalNumber implements SpiritType
{
	@Override
	public String getTypeName()
	{
		return "rational_number";
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
