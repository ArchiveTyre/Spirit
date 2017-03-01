#include <stdio.h>
#include <string.h>
#include "grammar.tab.h"
#include "ast.h"

/**
 * Just prints the help information. (^.^)
 */
static void help()
{
	printf("Usage: cheri [options] files...\n");
	printf("Options:\n");
	printf("\t--help\t\tDispays this help section.\n");
	printf("\t-o\t\tSpecifies where to place output.\n");
}

static int parse_file(char *filename)
{
	/* Call lexer. */
	extern int yylineno;
	extern FILE *yyin;

	ast_root_node = ast_make_root();
	ast_prev_node = ast_root_node;

	yyin = fopen(filename, "r");
	yyparse();
	fclose(yyin);
	printf("Parsed: %d lines!\n", --yylineno);

	debug_ast_node(ast_root_node, 0);
	return 0;

}

int main(int argc, char *args[])
{

	char* output_file = NULL;

	//FIXME: This is ugly \/
	char* input_files[30];
	int input_files_index = 0;

	/* Parse the arguments. */
	for(int arg_index = 1; arg_index < argc; arg_index++) {
		char* arg_to_parse = args[arg_index];
		if (arg_to_parse[0] == '-') {
			if (strcmp(arg_to_parse, "-o") == 0) {
				arg_index++;
				output_file = args[arg_index];
			}
			else if (strcmp(arg_to_parse, "-h") == 0 || strcmp(arg_to_parse, "--help") == 0) {
				help();
				return 0;
			}
			else {
				printf("Unknown switch: %s", arg_to_parse);
				return 1;
			}
		}
		else {
			input_files[input_files_index] = arg_to_parse;
			input_files_index++;
		}
	}

	printf("Output: %s\n", output_file);

	/* Parse each file given as input. */
	for(int i = 0; i < input_files_index; i++) {
		printf("Input %d: %s\n", i, input_files[i]);
		parse_file(input_files[i]);
	}

	return 0;
}
