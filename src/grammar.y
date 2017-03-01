%{
#include "stdio.h"
#include "ast.h"
#include "stdlib.h"
#include "string.h"

int yyerror(char *str);
int yylex(void);

int line_indent = 0;

%}

%union{
	int tok_int_val;
	char *tok_string_val;
	char *tok_op_val;
	char *tok_symbol_name;

	struct ASTNode *tok_ast_node;

}

%token <tok_int_val> TOKEN_INT_LITERAL
%token <tok_string_val> TOKEN_STRING
%token <tok_symbol_name> TOKEN_SYMBOL;

%token TOKEN_NEWLINE
%token TOKEN_ASSIGN
%token TOKEN_INDENT
%token TOKEN_COMMA


%type <tok_int_val> t_num_exp
%type <tok_ast_node> t_any_exp
%type <tok_ast_node> t_func_call
%type <tok_ast_node> t_call_args

%left TOKEN_MINUS TOKEN_PLUS
%left TOKEN_MULTIPLY TOKEN_DIVIDE
%nonassoc TOKEN_LPAREN TOKEN_RPAREN
%start prog

%%

prog:
    				| prog t_any_exp TOKEN_NEWLINE {ast_insert_node($2); line_indent = 0;}
    				| prog TOKEN_NEWLINE
					;

/* Firstly, we simplify any expression only containing numbers. */
t_num_exp:			TOKEN_INT_LITERAL { $$ = $1;}
					| t_num_exp TOKEN_PLUS t_num_exp { $$ = $1 + $3;}
					| t_num_exp TOKEN_MINUS t_num_exp { $$ = $1 - $3;}
					| t_num_exp TOKEN_MULTIPLY t_num_exp { $$ = $1 * $3;}
					| t_num_exp TOKEN_DIVIDE t_num_exp { $$ = $1 / $3;}
					| TOKEN_LPAREN t_num_exp TOKEN_RPAREN {$$ = $2;}
					;

/* t_any_exp is any expression. */
t_any_exp:
				| TOKEN_STRING {
					size_t len = strlen($1);
					char *new_str = malloc(len - 1);
					memcpy(new_str, $1+1, len-2);
					new_str[len - 2] = 0;
					$$ = ast_make_string(new_str);
					free(new_str);
				}
				| t_num_exp {$$ = ast_make_number($1);}
				| t_func_call {}
				| t_any_exp TOKEN_PLUS t_any_exp {$$ = ast_make_op("+", $1, $3); debug_ast_node($$, 0);}
				| t_any_exp TOKEN_MINUS t_any_exp {$$ = ast_make_op("-", $1, $3);}
				| t_any_exp TOKEN_MULTIPLY t_any_exp {$$ = ast_make_op("*", $1, $3);}
				| t_any_exp TOKEN_DIVIDE t_any_exp {$$ = ast_make_op("/", $1, $3);}
				| t_any_exp TOKEN_ASSIGN t_any_exp {$$ = ast_make_op("=", $1, $3);}
				| TOKEN_LPAREN t_any_exp TOKEN_RPAREN {$$ = $2;}
				| TOKEN_SYMBOL {$$ = ast_make_symbol($1);}
				;


t_func_call:		TOKEN_SYMBOL TOKEN_LPAREN t_call_args TOKEN_RPAREN {
	   					$$ = ast_make_func_call($1);
						$$->args_chain = $3;}
	   				;

/* Tries to connect one arg with another. */
t_call_args:			t_any_exp {}
						| t_call_args TOKEN_COMMA t_any_exp {$1->args_next = $3;}
						;

%%

int yyerror(char *str)
{

	extern int yylineno;
	extern char *yytext;

	printf("ERROR(%d): %s at sym \"%s\"\n", yylineno, str, yytext);
	fflush(stdin);
	extern void exit(int);
	exit(1);
}
