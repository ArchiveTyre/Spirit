#include "compile.h"
#include <stdlib.h>
#include <libgen.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "grammar.tab.h"
#include "debug/ast_debug.h"

/*** C BACKEND ***/
#include "cpp_backend.h"

const char *out_dir = "out";

CompileResult *compile_file(char *file_name)
{

	/*** EXTERNS ***/
	extern int yylineno;
	extern FILE *yyin;
	extern void yyrestart(FILE *file);

	/*** CREATE RESULT STRUCT ***/
	CompileResult *target = malloc(sizeof(CompileResult));
	current_compile_result = target;
	target->file_name = strdup(file_name);

	/*** FIND AND SET CLASS NAME ***/
	/* Get the class name based of the basnemae of the filename. */
	char *simple_filename_free = strdup(file_name); // FIXME: Add error checking here.
	char *simple_filename = basename(simple_filename_free);

	/* Finds the first occurance of "." and copies everything up until that point. */
	char *new_class_name = strndup(simple_filename,
		strchr(simple_filename, '.') - simple_filename);
	printf("Compiling class: %s\n", new_class_name);
	target->class_name = new_class_name;

	/*** OPEN FILE OUT ***/
	#define format_string "%s/%s.cpp"
	int size_needed = snprintf(NULL, 0, format_string, out_dir, simple_filename);
	target->out_file_name = malloc(size_needed + 1);
	sprintf(target->out_file_name, format_string, out_dir, simple_filename);
	target->out_file = fopen(target->out_file_name, "w");


	/*** CREATE OTHER RESULT STRUCTS ***/
	/* The parse needs somewhere to put the AST into. */
	target->ast_root_node = ast_make_root(new_class_name);

	/*** PARSE THE FILE INTO AST ***/
	/* Open up the file. */
	yyrestart(fopen(file_name, "r"));

	/* Compile the file. */
	int parse_result = yyparse();
	if (parse_result != 0) {
		printf("ERROR: Unrecoverable error: %d.\n", parse_result);
	}

	/* Close the file. */
	fclose(yyin);

	/* Debug. */
	printf("[DONE] Parsed: %d lines!\n", yylineno - 1);

#if DEBUG
	debug_ast_node(target->ast_root_node, false, true, 0);
#endif

	/* Before we can compile we need to create the symbol tree. */
	ast_make_sym_tree(target->ast_root_node);
	target->sym_entry = target->ast_root_node->symentry;

	/* Then we can compile into a C/C++ file*/
	// FIXME: Support multiple backends.
	compile_ast_to_cpp(target->ast_root_node, target->out_file, false, false, 0);

	target->success = true;

	/*** FREE STRINGS ***/
	free(simple_filename_free);

	return target;
}

void free_compile_result(CompileResult *target)
{
	/* Free the AST and the symbol table. */
	free(target->class_name);
	free(target->out_file_name);
	free_sym(target->sym_entry);
	free_ast_node(target->ast_root_node);
	free(target);
}
