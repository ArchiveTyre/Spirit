package compiler;

// FIXME: isKeyword and isOperator is arguably the only functions in this class that are needed for our compiler.

/**
 * @author david
 * @date 4/11/17.
 */
public class Syntax
{

	/** Used to declare variables. */
	public static final String KEYWORD_VAR				= "var"			;

	/** Used to declare constants. */
	public static final String KEYWORD_CON				= "con"			;

	/** Used to declare functions. */
	public static final String KEYWORD_FUN				= "fn"			;

	/** Used to declare an if statement*/
	public static final String KEYWORD_IF				= "if"			;

	/** Used to declare an else if statement. */
	public static final String KEYWORD_ELSEIF			= "elseif"		;

	/** Used to declare an else statement. */
	public static final String KEYWORD_ELSE				= "else"		;

	/** Used to declare a match statement  */
	public static final String KEYWORD_MATCH			= "match"		;

	public static final String KEYWORD_CASE				= "case"		;
	public static final String KEYWORD_LOOP				= "loop"		;
	public static final String KEYWORD_CLASS			= "class"		;
	public static final String KEYWORD_MY				= "my"			;
	public static final String KEYWORD_IMPORT			= "import"		;
	public static final String KEYWORD_USE_NAMESPACE	= "use"			;
	public static final String KEYWORD_THEN				= "then"		;
	public static final String KEYWORD_TYPE				= "type"		;
	public static final String KEYWORD_TYPE_OBJECT		= "object"		;
	public static final String KEYWORD_TYPE_INTERFACE	= "interface"	;
	public static final String KEYWORD_TYPE_ENUM		= "enum"		;
	public static final String KEYWORD_EXTEND			= "extends"		;

	public static final String COND_OPERATOR_EQU		= "is"			;
	public static final String COND_OPERATOR_AND		= "and"			;
	public static final String COND_OPERATOR_OR			= "or"			;
	public static final String COND_OPERATOR_NOT		= "not"			;
	public static final String COND_OPERATOR_XOR		= "xor"			;
	public static final String COND_OPERATOR_MOD		= "mod"			;

	public static final String OPERATOR_ACCESS			= "."			;
	public static final String OPERATOR_BLOCKSTART		= ":"			;
	public static final String OPERATOR_TYPESPECIFY		= ":"			;
	public static final String OPERATOR_RETURNTYPE		= ":"			;
	public static final String OPERATOR_RETURNVALUE		= "="			;

	public static final String MATH_OP_PLUS				= "+"			;
	public static final String MATH_OP_SUB				= "-"			;
	public static final String MATH_OP_MUL				= "*"			;
	public static final String MATH_OP_DIV				= "/"			;
	public static final String MATH_OP_POW				= "**"			;
	public static final String MATH_OP_ROOT				= "//"			;



	public static final String ASSIGN_EQU				= "="			;
	public static final String ASSIGN_ADD				= "+="			;
	public static final String ASSIGN_SUB				= "-="			;
	public static final String ASSIGN_MUL				= "*="			;
	public static final String ASSIGN_DIV				= "/="			;
	public static final String ASSIGN_POW				= "**="			;
	public static final String ASSIGN_ROOT				= "//="			;
	public static final String ASSIGN_INC				= "++"			;
	public static final String ASSIGN_DEC				= "--"			;



	public class Keyword
	{

		public static final String IF		= "if"			;
		public static final String ELSE		= "else"		;
		public static final String ELSEIF	= "elseif"		;
		public static final String MATCH	= "match"		;
		public static final String LOOP		= "loop"		;
		public static final String MY		= "my"			;
		public static final String IMPORT	= "import"		;
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

		public static final String ACCESS	=	"."	;
		public static final String TYPEDEF	= 	":"	;
		public static final String FUNCVAL	= 	"=" ;
		public static final String MAP		= 	"=>";


	}

	public class Type
	{
		public static final String CHAR		= "char"	;
		public static final String WCHAR	= "wchar"	;

		public static final String INT8		= "int8"	;
		public static final String INT16	= "int16"	;
		public static final String INT		= "int"		;
		public static final String INT64	= "int64"	;

		public static final String UINT8	= "uint8"	;
		public static final String UINT16	= "uint16"	;
		public static final String UINT		= "uint"	;
		public static final String UINT64	= "uint64"	;

		public static final String FLOAT	= "float"	;
		public static final String DOUBLE	= "double"	;

	}


	public static boolean isKeyword(String string)
	{
		switch (string)
		{
			case KEYWORD_VAR			:
			case KEYWORD_CON			:
			case KEYWORD_FUN			:
			case KEYWORD_IF			 	:
			case KEYWORD_ELSE			:
			case KEYWORD_ELSEIF			:
			case KEYWORD_MATCH			:
			case KEYWORD_CASE			:
			case KEYWORD_LOOP			:
			case KEYWORD_CLASS			:
			case KEYWORD_MY				:
			case KEYWORD_IMPORT			:
			case KEYWORD_USE_NAMESPACE	:
			case KEYWORD_THEN			:
			case KEYWORD_TYPE			:
			case KEYWORD_TYPE_OBJECT	:
			case KEYWORD_TYPE_INTERFACE	:
			case KEYWORD_TYPE_ENUM		:
				return true;
			default:
				return false;
		}
	}

	// FIXME: These might be unnecessary.

	public static boolean isConditionalOperator(String string)
	{
		switch (string)
		{
			case COND_OPERATOR_EQU		:
			case COND_OPERATOR_AND		:
			case COND_OPERATOR_OR		:
			case COND_OPERATOR_NOT		:
			case COND_OPERATOR_XOR		:
				return true;
			default:
				return false;
		}
	}

	public static boolean isOperator(String string)
	{
		switch (string)
		{
			case OPERATOR_ACCESS		:
			case OPERATOR_BLOCKSTART	:
				return true;
			default:
				return false;
		}
	}



}

