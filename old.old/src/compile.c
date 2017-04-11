#include "compile.h"
#include <stdlib.h>
#include <libgen.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <sys/stat.h>
#include <time.h>
#include <errno.h>
#include "grammar.tab.h"
#include "debug/ast_debug.h"
#include "debug/sym_debug.h"
#include "analyse.h"

/*** C BACKEND ***/
#include "backend/cpp_backend.h"

const char *out_dir = "out";
CompileResult *current_compile_result = NULL;
CompileResult *newest_compile_result = NULL;

static bool needs_recompile(char *src_file, char *backend_out_file)
{
	struct stat src_stat, backend_out_stat;

	/* If src file does not exist we have a problem in the compiler code. */
    if (stat(src_file, &src_stat) != 0) {
        printf("ERROR: Could not get stat for src file: %s\n", src_file);
		abort();
        return 1;
    }

	/* If backend out file does not exist then clearly we have to recompile. */
	else if (stat(backend_out_file, &backend_out_stat) != 0) {
		return true;
	}

	/* Return the value of the check that src is newer than out. */
    return difftime(src_stat.st_mtime, backend_out_stat.st_mtime) >= 0;
}

static int mkpath(char *dir, mode_t mode)
{
    struct stat sb;

    if (!dir) {
		printf("ERROR: Can't mkpath on null.");
		abort();
        return 1;
    }

	/* Check if already done. */
    if (!stat(dir, &sb))
        return 0;

	char *dir_name = alloca(strlen(dir) + 1);
	strcpy(dir_name, dir);
    mkpath(dirname(dir_name), mode);

    return mkdir(dir, mode);
}

CompileResult *compile_file(char *file_name)
{

	/*** EXTERNS ***/
	extern int yylineno;
	extern FILE *yyin;
	extern void yyrestart(FILE *file);

	/*** CREATE RESULT STRUCT ***/
	CompileResult *target = malloc(sizeof(CompileResult));
	target->prev_result = newest_compile_result;
	newest_compile_result = target;
	CompileResult *previous_result = current_compile_result;
	current_compile_result = newest_compile_result;
	target->file_name = strdup(file_name);

	/*** ASSURE THT THE OUT DIR EXISTS ***/
	struct stat dir_stat = {0};
	if (stat(out_dir, &dir_stat) == -1) {
    	mkdir(out_dir, 0755);
	}

	/*** FIND AND SET CLASS NAME ***/
	/* Get the class name based of the basnemae of the filename. */
	char *simple_filename_free = strdup(file_name);
	char *simple_filename = basename(simple_filename_free);

	/* Finds the first occurance of "." and copies everything up until that point. */
	char *new_class_name = strndup(simple_filename, strchr(simple_filename, '.') - simple_filename);
	printf("Compiling class: %s\n", new_class_name);
	target->class_name = new_class_name;

	/*** CREATE OUT & SYM FILE NAME ***/
	#define format_string "%s/%s.cpp"
	int size_needed = snprintf(NULL, 0, format_string, out_dir, file_name);
	target->out_file_name = malloc(size_needed + 1);
	sprintf(target->out_file_name, format_string, out_dir, file_name);
	#undef format_string

	#define format_string "%s/%s.sym"
	int size_needed_2 = snprintf(NULL, 0, format_string, out_dir, file_name);
	char *symbol_file_name = malloc(size_needed_2 + 1);
	sprintf(symbol_file_name, format_string, out_dir, file_name);
	#undef format_string

	/*** CHECK IF NEEDS RECOMPILATION. ***/
	if (needs_recompile(file_name, target->out_file_name)) {

		/* Make sure that the out dir exists. */
		char *dir_name = alloca(strlen(target->out_file_name + 1));
		strcpy(dir_name, target->out_file_name);
		mkpath(dirname(dir_name), 0777);

		/* Open the file. */
		target->out_file = fopen(target->out_file_name, "w");

		/*** CREATE OTHER RESULT STRUCTS ***/
		/* The parse needs somewhere to put the AST into. */
		target->ast_root_node = ast_make_root(new_class_name);

		/*** PARSE THE FILE INTO AST ***/
		/* Open up the file. */
		yyrestart(fopen(file_name, "r"));
		yylineno = 1;

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

		/* Analyse and fix the AST before compiling it out. */
		analyse_and_fix_class(target);

		/* Then we can compile into a C/C++ file*/
		// FIXME: Support multiple backends.
		compile_ast_to_cpp(target->ast_root_node, target->out_file, false, false, 0);

		/*** SAVE SYMBOL TABLE ***/
		FILE *symbol_file = fopen(symbol_file_name, "w");

		sym_save_to_file(target->sym_entry, symbol_file);

		fclose(symbol_file);

	}

	/*** IN CASE THE CLASS IS ALREADY COMPILED ***/
	else {
		printf("Already compiled!\n");
		target->ast_prev_node = NULL;
		target->ast_root_node = NULL;
		target->out_file = NULL;

		/* Load symbol file. */
		FILE *symbol_file = fopen(symbol_file_name, "r");
		target->sym_entry = sym_load_from_file(symbol_file);
		fclose(symbol_file);
	}

#ifdef DEBUG
	debug_symbol(current_compile_result->sym_entry, 0);
#endif

	target->success = true;
	current_compile_result = previous_result;

	/*** FREE STRINGS ***/
	free(simple_filename_free);

	return target;
}

void free_compile_result(CompileResult *target)
{
	free(target->class_name);
	free(target->file_name);
	free(target->out_file_name);
	if (target->sym_entry != NULL)
		free_sym(target->sym_entry);
	if (target->ast_root_node != NULL)
		free_ast_node(target->ast_root_node);
	if (target->out_file != NULL)
		fclose(target->out_file);
	target->out_file = NULL;
	free(target);
}
