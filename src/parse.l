%{

// FIXME: Maybe we should name "symbols" to "identifiers".

#include <stdio.h>

/* Generated from "grammar.y". */
#include "grammar.tab.h"

/*
 * "yytext" is a string containing the matched text.
 * "yylineno" is self-explanatory.
 */

extern void yyerror(char *str);
extern int line_indent;
%}

%option stack
%option yylineno
%option nounput
%option noyy_top_state
%option noinput

%x comment_state

%%

"/*" {
	yy_push_state(comment_state);
}
<comment_state>{
	"\\/" {
		/* Escape char. */
	}
	"*/" {
		/* Exit comment. */
		yy_pop_state();
	}
	"/*" {
		/* Recursive comment. */
		yy_push_state(comment_state);
	}
	\n {
	}
	. {
		/* Do nothing. */
	}
}

\"(\\.|[^"])*\" { //" /* For strings. */
	yylval.tok_as_string = strdup(yytext);
	return TOKEN_STRING;
}

#inline(.)*#endinline {

	/* Skip #include. */
	char *text = yytext + strlen("#inline");

	/* Cut the #endinline at the end of the string. */
	char *end = text;

	/* To do that we find the end of the string. s*/
	while (*end++)
    ;

	/* And terminate it with a zero from the point where endinline begins. */
	text[strlen(text) - strlen("#endinline")] = 0;
	yylval.tok_as_string = strdup(text);

	return TOKEN_INLINE;
}

\t {
	// FIXME: If someone uses a tab after the sentence we'll have trouble!
	++line_indent;
#ifdef DEBUG
	printf("Inc indentation...\n");
#endif
}

: {
	return TOKEN_COLON;
}

"->" {
	return TOKEN_ARROW;
}

"+" {
	return TOKEN_PLUS;
}

"-" {
	return TOKEN_MINUS;
}

"*" {
	return TOKEN_MULTIPLY;
}

"/" {
	return TOKEN_DIVIDE;
}

"==" {
	return TOKEN_COMPARISON;
}

"=" {
	return TOKEN_ASSIGN;
}

"fun" {
	return TOKEN_FUNCTION;
}

"var" {
	return TOKEN_VARIABLE;
}

\( {
	return TOKEN_LPAREN;
}

\) {
	return TOKEN_RPAREN;
}


"," {
	return TOKEN_COMMA;
}

[0-9]+ {
	yylval.tok_int_val = atoi(yytext);
	return TOKEN_INT_LITERAL;
}

[a-zA-Z0-9_\.]+ {
	yylval.tok_as_string = strdup(yytext);
	return TOKEN_SYMBOL;
}

\n {
	return TOKEN_NEWLINE;
}

[ ] {
	/* Spaces are ignored... */
}

. {
	yyerror("Unexpected symbol");
}

%%
