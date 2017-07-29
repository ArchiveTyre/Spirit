package compiler.builtins;

import compiler.SpiritType;

/**
 * @author david
 * @date 4/12/17.
 */
public class Builtins
{



	private static SpiritType[] builtins = {
			new TypeRationalNumber(),
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

	public static SpiritType getBuiltin(String name)
	{
		// FIXME: This is so slow onii-chan. I want a direct reference to the builtins too. //

		for (SpiritType type : builtins)
		{
			if (name.equals(type.getTypeName()))
			{
				return type;
			}
		}

		// FIXME: Is this the best way?
		return new TypeUndefined(name);
	}
}
