package compiler.builtins;

import compiler.CherryType;

/**
 * Created by david on 4/12/17.
 */
public class Builtins
{

	private static CherryType[] builtins = {
			new TypeInteger(),
	};

	public static CherryType getBuiltin(String name)
	{
		for (CherryType type : builtins)
		{
			if (name.equals(type.getName()))
			{
				return type;
			}
		}
		return null;
	}
}
