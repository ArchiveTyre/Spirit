#include <stdio.h>
#include <libgen.h>
#include <string.h>
#include <stdlib.h>
#include "grammar.tab.h"
#include "ast.h"
#include "symtbl.h"
#include "cpp_backend.h"

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
static int parse_file(char *filename, FILE *out)
{
	/* Call lexer. */
	extern int yylineno;
	extern FILE *yyin;
	extern void yylex_destroy();

	/* Get the classname based of the basnemae of the filename. */
	// FIXME: Add error checking here.
	char *simple_filename_free = strdup(filename);
	char *simple_filename = basename(strdup(simple_filename_free));

	/* Finds the first occurance of "." and copies everything up until that point. */
	char *new_classname = strndup(simple_filename,
		strchr(simple_filename, '.') - simple_filename);
	printf("Classname: %s\n", new_classname);

	/* The parse needs somewhere to put the AST into. */
	ast_root_node = ast_make_root(new_classname);
	ast_prev_node = ast_root_node;

	/* Do the parsing. */
	yyin = fopen(filename, "r");
	// FIXME: Check result of function for errors.
	yyparse();
	fclose(yyin);
	printf("Parsed: %d lines!\n", yylineno - 1);

#if DEBUG
	debug_ast_node(ast_root_node, 0);
#endif

	/* Before we can compile we need to create the symbol tree. */
	ast_make_sym_tree(ast_root_node);

	/* Then we can compile into a C/C++ file*/
	compile_ast_to_cpp(ast_root_node, out);

	/* Free the AST and the symbol table. */
	free_sym(ast_root_node->symentry);
	free_ast_node(ast_root_node);

	free(simple_filename_free);
	free(new_classname);

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
	char* output_filename = NULL;

	/* Parse the arguments. */
	for(int arg_index = 1; arg_index < argc; arg_index++) {
		char* arg_to_parse = args[arg_index];
		if (arg_to_parse[0] == '-') {

			/* The output flag. */
			if (strcmp(arg_to_parse, "-o") == 0) {
				arg_index++;
				output_filename = args[arg_index];
			}

			/* The help option. */
			else if (strcmp(arg_to_parse, "-h") == 0 || strcmp(arg_to_parse, "--help") == 0) {
				help();
				return 0;
			}

			/* Show an error. */
			else {
				printf("Unknown switch: %s", arg_to_parse);
				return 1;
			}
		}

		/* If this wasn't an option than we append to list of files to compile. */
		else {
			files_list = &(FilesList){files_list, arg_to_parse};
		}
	}
#ifdef DEBUG
	printf("Output: %s\n", output_filename);
#endif

	/* Open the output file. If none specified use the stdout. */
	FILE *output_file = output_filename != NULL ? fopen(output_filename, "w") : stdout;


	/* Parse each file given as input. */
	while(files_list != NULL) {
#if DEBUG
		printf("Parsing: %s\n", files_list->filename);
#endif
		parse_file(files_list->filename, output_file);
		files_list = files_list->prev;
	}

	return 0;
}
