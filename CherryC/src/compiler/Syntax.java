package compiler;

/**
 * Created by david on 4/11/17.
 */
public class Syntax
{

	public static final String KEYWORD_VAR				= "var";
	public static final String KEYWORD_CON				= "con";
	public static final String KEYWORD_FUN				= "fun";
	public static final String KEYWORD_IF				= "if";
	public static final String KEYWORD_MATCH			= "match";
	public static final String KEYWORD_CASE				= "case";
	public static final String KEYWORD_LOOP				= "loop";
	public static final String KEYWORD_CLASS			= "class";

	public static final String COND_OPERATOR_EQU		= "is";
	public static final String COND_OPERATOR_AND		= "and";
	public static final String COND_OPERATOR_OR			= "or";
	public static final String COND_OPERATOR_NOT		= "not";

	public static final String OPERATOR_ACCESS			= ".";
	public static final String OPERATOR_BLOCKSTART		= ":";
	public static final String OPERATOR_TYPESPECIFY		= ":";
	public static final String OPERATOR_RETURNTYPE		= "->";

	public static boolean isToken(String string)
	{
		switch (string)
		{
			case KEYWORD_VAR			:
			case KEYWORD_CON			:
			case KEYWORD_FUN			:
			case KEYWORD_IF			 	:
			case KEYWORD_MATCH			:
			case KEYWORD_CASE			:
			case KEYWORD_LOOP			:
			case KEYWORD_CLASS			:
			case COND_OPERATOR_EQU		:
			case COND_OPERATOR_AND		:
			case COND_OPERATOR_OR		:
			case COND_OPERATOR_NOT		:
			case OPERATOR_ACCESS		:
			case OPERATOR_BLOCKSTART	:
			case OPERATOR_RETURNTYPE	:
				return true;
			default:
				return false;
		}
	}

	public static boolean isKeyword(String string)
	{
		switch (string)
		{
			case KEYWORD_VAR			:
			case KEYWORD_CON			:
			case KEYWORD_FUN			:
			case KEYWORD_IF			 	:
			case KEYWORD_MATCH			:
			case KEYWORD_CASE			:
			case KEYWORD_LOOP			:
			case KEYWORD_CLASS			:
				return true;
			default:
				return false;
		}
	}

	public static boolean isConditionalOperator(String string)
	{
		switch (string)
		{
			case COND_OPERATOR_EQU		:
			case COND_OPERATOR_AND		:
			case COND_OPERATOR_OR		:
			case COND_OPERATOR_NOT		:
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
			case OPERATOR_RETURNTYPE	:
				return true;
			default:
				return false;
		}
	}


}

