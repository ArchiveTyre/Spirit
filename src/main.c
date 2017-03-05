#include <stdio.h>
#include <string.h>
#include "grammar.tab.h"
#include "ast.h"
#include "symtbl.h"

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

/**
 * Parses one file and creates an AST for it.
 */
static int parse_file(char *filename)
{
	/* Call lexer. */
	extern int yylineno;
	extern FILE *yyin;
	extern void yylex_destroy();

	ast_root_node = ast_make_root();
	ast_prev_node = ast_root_node;
	global_symbol_table = sym_define("root", "root", NULL);

	yyin = fopen(filename, "r");
	yyparse();
	fclose(yyin);
	printf("Parsed: %d lines!\n", yylineno - 1);

#if DEBUG
	debug_ast_node(ast_root_node, 0);
#endif
	free_ast_node(ast_root_node);
	free_sym(global_symbol_table);

	yylex_destroy();

#if DEBUG
	printf("Cake delicious desu. You must eait it ~desu.\n");
#endif
	return 0;

}

int main(int argc, char *args[])
{

	typedef struct FilesList FilesList;
	struct FilesList {
		FilesList *prev;
		char *filename;
	};

	FilesList *files_list = NULL;
	char* output_file = NULL;

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
			printf("Ins: %s\n", arg_to_parse);
			files_list = &(FilesList){files_list, arg_to_parse};
		}
	}

	printf("Output: %s\n", output_file);

	/* Parse each file given as input. */
	while(files_list != NULL) {
#if DEBUG
		printf("Parsing: %s\n", files_list->filename);
#endif
		parse_file(files_list->filename);
		files_list = files_list->prev;
	}

	return 0;
}
