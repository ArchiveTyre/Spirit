package compiler.builtins;

import compiler.Syntax;

/**
 * @author david
 * @date 4/19/17.
 */
public enum FileType
{
	OBJECT,
	INTERFACE,
	ENUM,
	UNDEFINED;


	public static FileType toFileType(String symbol)
	{
		switch (symbol)
		{
			case Syntax.Definable.OBJECT:
				return OBJECT;
			case Syntax.Definable.INTERFACE:
				return INTERFACE;
			case Syntax.Definable.ENUM:
				return ENUM;
			default:
				return UNDEFINED;
		}
	}
}
