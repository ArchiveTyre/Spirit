%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ast.h"

int yyerror(char *str);
int yylex(void);

int line_indent = 0;

%}

%union{
	int tok_int_val;
	char *tok_as_string;

	struct ASTNode *tok_ast_node;

}

%token <tok_int_val> TOKEN_INT_LITERAL
%token <tok_as_string> TOKEN_STRING
%token <tok_as_string> TOKEN_SYMBOL;

%token TOKEN_NEWLINE
%token TOKEN_COLON
%token TOKEN_ASSIGN
%token TOKEN_INDENT
%token TOKEN_COMMA

%type <tok_int_val> t_num_exp
%type <tok_ast_node> t_any_exp
%type <tok_ast_node> t_func_call
%type <tok_ast_node> t_call_args
%type <tok_ast_node> t_block

/*
 * The order of these statements matter because they tell us in what order
 * to evluate & reduce tokens.
 */

%nonassoc TOKEN_LPAREN TOKEN_RPAREN
%nonassoc TOKEN_COLON
%nonassoc TOKEN_SYMBOL

%nonassoc TOKEN_COMPARISON
%left TOKEN_ASSIGN
%left TOKEN_MINUS TOKEN_PLUS
%left TOKEN_MULTIPLY TOKEN_DIVIDE


%start program

%%

/* A program consists of lines. */
program:		%empty
				| program line
				;

/* Each line could either be an expression or a block definition.*/
line:			TOKEN_NEWLINE
				| t_block TOKEN_COLON TOKEN_NEWLINE {ast_insert_node($1);}
				| t_any_exp TOKEN_NEWLINE {ast_insert_node($1);}
				;


/* Firstly, we simplify any expression only containing numbers. */
t_num_exp:		TOKEN_INT_LITERAL { $$ = $1;}
				| t_num_exp TOKEN_PLUS t_num_exp { $$ = $1 + $3;}
				| t_num_exp TOKEN_MINUS t_num_exp { $$ = $1 - $3;}
				| t_num_exp TOKEN_MULTIPLY t_num_exp { $$ = $1 * $3;}
				| t_num_exp TOKEN_DIVIDE t_num_exp { $$ = $1 / $3;}
				| TOKEN_LPAREN t_num_exp TOKEN_RPAREN {$$ = $2;}
				;

/* t_any_exp is any expression. Strings, numbers, function calls, etc */
t_any_exp:		TOKEN_SYMBOL {$$ = ast_make_symbol($1); free($1);}
				| TOKEN_STRING {
					$1[strlen($1) - 1] = 0;
					$$ = ast_make_string($1+1);
					free($1);}
				| t_num_exp {$$ = ast_make_number($1);}
				| t_any_exp TOKEN_PLUS t_any_exp {$$ = ast_make_op("+", $1, $3);}
				| t_any_exp TOKEN_MINUS t_any_exp {$$ = ast_make_op("-", $1, $3);}
				| t_any_exp TOKEN_MULTIPLY t_any_exp {$$ = ast_make_op("*", $1, $3);}
				| t_any_exp TOKEN_DIVIDE t_any_exp {$$ = ast_make_op("/", $1, $3);}
				| t_any_exp TOKEN_ASSIGN t_any_exp {$$ = ast_make_op("=", $1, $3);}
				| t_any_exp TOKEN_COMPARISON t_any_exp {$$ = ast_make_op("==", $1, $3);}
				| TOKEN_LPAREN t_any_exp TOKEN_RPAREN {$$ = $2;}
				| t_func_call {$$ = $1;}
				;

/* Blocks end with a colon. */
t_block:		TOKEN_SYMBOL t_call_args {
					$$ = ast_make_block($1);
					$$->args_chain = $2;
					free($1);}
				;

/* A basic function call. */
t_func_call:	TOKEN_SYMBOL TOKEN_LPAREN t_call_args TOKEN_RPAREN {
	   				$$ = ast_make_func_call($1);
					$$->args_chain = $3;
					free($1);}
				| TOKEN_SYMBOL t_any_exp {
					$$ = ast_make_func_call($1);
					$$->args_chain = $2;
					free($1);}
	   			;

/* Tries to connect one arg with another. A function could have no arguments. */
t_call_args:	%empty {$$ = ast_make_default_arg();}
				| t_any_exp {$$ = $1;}
				| t_call_args TOKEN_COMMA t_any_exp {$1->args_next = $3;}
				;

%%

int yyerror(char *str)
{

	extern int yylineno;
	extern char *yytext;

	printf("ERROR(line: %d): %s at sym \"%s\"\n", yylineno, str, yytext);
	fflush(stdin);
	extern void exit(int);
	exit(1);
}
