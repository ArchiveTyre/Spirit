#include <stdio.h>
#include <libgen.h>
#include <string.h>
#include <stdlib.h>
#include "grammar.tab.h"
#include "ast.h"
#include "debug/ast_debug.h"
#include "symtbl.h"
#include "cpp_backend.h"
#include "compile.h"

/**
 * Just prints the help information. (^.^)
 */
static void help()
{
	printf("Usage: cheri [options] files...\n");
	printf("Options:\n");
	printf("\t--help        Dispays this help section.\n");
	printf("\t-o            Specifies where to place output binary.\n");
	printf("\t--out-dir     Specifies where to place build files.\n");
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


			/* Set the output dir. */
			else if (strcmp(arg_to_parse, "--out-dir") == 0) {
				arg_index++;
				out_dir = args[arg_index];
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
			/* Append to list. */
			FilesList* f = malloc(sizeof(FilesList));
			f->filename = arg_to_parse;
			f->prev = files_list;
			files_list = f;
		}
	}
#ifdef DEBUG
	printf("Output: %s\n", output_filename);
#endif

	/* Open the output file. If none specified use the stdout. */
	out_file = output_filename != NULL ? fopen(output_filename, "w") : stdout;


	/* Parse each file given as input. */
	while(files_list != NULL) {
#if DEBUG
		printf("Parsing: %s\n", files_list->filename);
#endif
		compile_file(files_list->filename);
		FilesList *old = files_list;
		files_list = files_list->prev;
		free(old);

	}

	return 0;
}
