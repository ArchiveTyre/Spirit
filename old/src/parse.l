%{
/*
 * "yytext" is a string containing the matched text.
 * "yylineno" is self-explanatory.
 */

// FIXME Remove.
#include <stdio.h>

/* Generated from "grammar.y". */
#include "grammar.tab.h"

extern int yyerror(char *str);
extern int line_indent;

char *inline_buffer;
int inline_buffer_write;
int inline_buffer_allocated_size;
%}

%option stack
%option yylineno
%option nounput
%option noyy_top_state
%option noinput

%x comment_state
%x inline_state

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

#inline {
	/* Allocate a new buffer.*/
	inline_buffer_allocated_size = 1;
	inline_buffer = malloc(inline_buffer_allocated_size);
	inline_buffer_write = 0;

	/* Null teminate it. */
	inline_buffer[0] = 0;

	yy_push_state(inline_state);
}

<inline_state>{
	#endinline {
		/* Give the buffer to the token and don't free it. */
		yylval.tok_as_string = inline_buffer;
		yy_pop_state();
		return TOKEN_INLINE;
	}
	.|\n {
		if (inline_buffer_write >= inline_buffer_allocated_size) {
			inline_buffer_allocated_size = inline_buffer_allocated_size * 2;
			inline_buffer = realloc(inline_buffer, inline_buffer_allocated_size);
			// FIXME: Check for ERROR...
		}

		/* Insert the character. */
		inline_buffer[inline_buffer_write] = yytext[0];

		/* Increment write. */
		++inline_buffer_write;

		/* Null terminate. */
		inline_buffer[inline_buffer_write] = 0;
	}
}

\t {
	// FIXME: If someone uses a tab after the sentence we'll have trouble ~desu!
	++line_indent;
#ifdef DEBUG
	printf("Inc indentation...\n");
#endif
}

: {
	return TOKEN_COLON;
}

"\." {
	return TOKEN_ACCESS;
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

[a-zA-Z0-9_]+ {
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
