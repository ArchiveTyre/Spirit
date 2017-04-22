package compiler.builtins;

import compiler.Syntax;

/**
 * Created by david on 4/19/17.
 */
public enum FileType
{
	OBJECT,
	INTERFACE,
	ENUM,
	UNDEFINED;


	public static FileType toFileType(String symbol)
	{
		// FIXME: I don't like this function. B-baka! ^^

		switch (symbol)
		{
			case Syntax.Type.OBJECT:
				return OBJECT;
			case Syntax.Type.INTERFACE:
				return INTERFACE;
			case Syntax.Type.ENUM:
				return ENUM;
			default:
				return UNDEFINED;
		}
	}
}
