package compiler.builtins;

import compiler.SpiritType;
import compiler.ast.ASTBase;

import java.util.ArrayList;

/**
 * Created by david on 7/6/17.
 */
public class TypeUndefined implements SpiritType
{

	private String typeName;

	public TypeUndefined(String typeName)
	{
		this.typeName = typeName;
	}

	@Override
	public String getTypeName()
	{
		return typeName;
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
}
