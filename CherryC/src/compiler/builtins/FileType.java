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
		switch (symbol)
		{
			case Syntax.KEYWORD_TYPE_OBJECT:
				return OBJECT;
			case Syntax.KEYWORD_TYPE_INTERFACE:
				return INTERFACE;
			case Syntax.KEYWORD_TYPE_ENUM:
				return ENUM;
			default:
				return UNDEFINED;
		}
	}
}
