package compiler;

/**
 * Defines the keywords and operators.
 *
 * FIXME: Is this really the right name for this class?
 * @author david
 * @date 4/11/17.
 */
@SuppressWarnings("ALL") // TODO: This is dangerous...
public class Syntax
{
	public class ReservedNames
	{
		public static final String CONSTRUCTOR = "new";
		public static final String OBJECT_CLASS = "Object";
	}

	public class Keyword
	{
		public static final String IF		= "if"			;
		public static final String ELSE		= "else"		;
		public static final String ELSEIF	= "elseif"		;
		public static final String MATCH	= "match"		;
		public static final String LOOP		= "loop"		;
		public static final String MY		= "my"			;
		public static final String IMPORT	= "import"		;
		public static final String FROM		= "from"		;
		public static final String USE		= "use"			;
		public static final String THEN		= "then"		;
		public static final String TYPE		= "type"		;
		public static final String EXTENDS	= "extends"		;
	}

	public class Op
	{
		public class Math
		{
			public static final String ADD	= "+"	;
			public static final String SUB	= "-"	;
			public static final String MUL	= "*"	;
			public static final String DIV	= "/"	;
			public static final String POW	= "**"	;
			public static final String ROT	= "//"	;
		}

		public class Cond
		{
			public class Txt
			{
				public static final String EQU		= "is"		;
				public static final String NOT_EQU	= "isnt"	;
				public static final String NOT		= "not"		;
				public static final String AND		= "and"		;
				public static final String OR		= "or"		;
				public static final String XOR		= "xor"		;
			}

			public static final String EQU			= "=="	;
			public static final String NOT_EQU		= "~="	;
			public static final String NOT			= "~"	;
			public static final String AND			= "&&"	;
			public static final String OR			= "||"	;
			public static final String XOR			= "^^"	;

		}

		public class Assign
		{
			public static final String IS	= "="	;
			public static final String ADD	= "+="	;
			public static final String SUB	= "-="	;
			public static final String MUL	= "*="	;
			public static final String DIV	= "/="	;
			public static final String POW	= "**="	;
			public static final String ROT 	= "//="	;
			public static final String INC	= "++"	;
			public static final String DEC	= "--"	;
		}

		public static final String 	ACCESS				=	"."		;
		public static final String 	TYPEDEF				= 	":"		;
		public static final String 	RETURN 				= 	"=" 	;
		public static final String 	MAP					= 	"=>"	;
		public static final String 	ARG_SEP				=	","		;
		public static final String 	BLOCK_COMMENT_START	=	"<%"	;
		public static final String	BLOCK_COMMENT_END	=	"%>"	;
		public static final char	INLINE_COMMENT		=	'%'		;
		public static final char	ANON_FUNC_START		=	'{'		;
		public static final char	ANON_FUNC_END		=	'}'		;
		public static final char 	TEMP_VAR			= 	'_'		;


	}

	public class Type
	{
		public static final String CHAR			= "char"		;
		public static final String WCHAR		= "wchar"		;
		public static final String INT8			= "int8"		;
		public static final String INT16		= "int16"		;
		public static final String INT			= "int"			;
		public static final String INT64		= "int64"		;
		public static final String UINT8		= "uint8"		;
		public static final String UINT16		= "uint16"		;
		public static final String UINT			= "uint"		;
		public static final String UINT64		= "uint64"		;
		public static final String FLOAT		= "float"		;
		public static final String DOUBLE		= "double"		;
		public static final String STRING		= "string"		;
		public static final String CONSTANT		= "const"		;
		public static final String OBJECT		= "object"		;
		public static final String ENUM			= "enum"		;
		public static final String INTERFACE	= "interface"	;
		public static final String FUNC			= "    function"	;

	}

	public class Macro
	{
		public static final char IDENTIFIER	=	'#'		;
		public static final String INLINE	=	"inline";
		public static final String END		=	"end"	;
	}



	public static boolean isKeyword(String string)
	{
		switch (string)
		{
			case Keyword.IF			 	:
			case Keyword.ELSE			:
			case Keyword.ELSEIF			:
			case Keyword.MATCH			:
			//case Keyword.CASE			:
			case Keyword.LOOP			:
			//case KEYWORD_MY			:
			case Keyword.IMPORT			:
			//case Keyword.USE_NAMESPACE:
			case Keyword.THEN			:
			case Keyword.TYPE			:
			case Type.OBJECT			:
			case Type.INTERFACE			:
			case Type.ENUM				:
			case Keyword.FROM			:
				return true;
			default:
				return false;
		}
	}

	public static String getOperator(String token)
	{
		switch (token)
		{
			case Op.Cond.Txt.EQU:
				return Op.Cond.EQU;
			case Op.Cond.Txt.NOT_EQU:
				return Op.Cond.NOT_EQU;
			case Op.Cond.Txt.AND:
				return Op.Cond.AND;
			case Op.Cond.Txt.OR:
				return Op.Cond.OR;
			case Op.Cond.Txt.XOR:
				return Op.Cond.XOR;
			case Op.Cond.Txt.NOT:
				return Op.Cond.Txt.NOT;
			default:
				return token;
		}
	}

	public static boolean isOperator(String t)
	{
		switch (t)
		{
			case Op.Cond.Txt.EQU:
			case Op.Cond.Txt.NOT_EQU:
			case Op.Cond.Txt.AND:
			case Op.Cond.Txt.OR:
			case Op.Cond.Txt.XOR:
			case Op.Cond.Txt.NOT:
				return true;
			default:
				return false;
		}
	}
}

