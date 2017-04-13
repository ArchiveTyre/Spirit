package compiler.builtins;

import compiler.CherryType;

/**
 * Created by david on 4/12/17.
 */
public class Builtins
{

	private static CherryType[] builtins = {
			new TypeInteger(),
			new TypeBool(),
			new TypeChar(),
			new TypeDouble(),
			new TypeFloat(),
			new TypeLong(),
			new TypeShort(),
			new TypeString(),
			new TypeVoid(),
			new TypeFunction(),
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
