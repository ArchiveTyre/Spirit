#include <vector>
#include <string>
#include <string.h>
#include "AST/ASTBase.hpp"
#include "ClassCompile.hpp"
#include "Lexer.hpp"

using std::string;

static void print_version()
{
	printf("Cheri v0.0.1\n");
	printf("© 2017 TYREREXUS ALL RIGHTS RESERVED\n");
}


/**
 * Just prints the help information. (^.^)
 */
static void help()
{
	printf("Usage: cheri [options] files...\n");
	printf("Options:\n");
	printf("    --help        Dispays this help section.\n");
	printf("    -v            Prints out the version\n");
	printf("    -o            Specifies where to place output binary.\n");
	printf("    --out-dir     Specifies where to place build files.\n");
}

int main(int argc, char *args[])
{

	std::vector<string> files_list;
	char* output_filename = NULL;

	// FIXME: Add a separate file for this.
	/*** PARSE ARGUMENTS ***/
	for(int arg_index = 1; arg_index < argc; arg_index++) {
		char* arg_to_parse = args[arg_index];
		if (arg_to_parse[0] == '-') {

			/* The output flag. */
			if (strcmp(arg_to_parse, "-o") == 0) {
				arg_index++;
				output_filename = args[arg_index];
			}

			else if (strcmp(arg_to_parse, "-v") == 0) {
				print_version();

				// FIXME: Do proper clean-up maybe...
				return 0;
			}

			/* Set the output dir. */
			else if (strcmp(arg_to_parse, "--out-dir") == 0) {
				arg_index++;
				ClassCompile::default_out_dir = args[arg_index];
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
			files_list.push_back(arg_to_parse);
		}
	}

	#ifdef DEBUG
		printf("Output: %s\n", output_filename);
	#endif
		
	Lexer::unitTest();


	/* Open the output file. If none specified use the stdout. */
	//out_file = output_filename != NULL ? fopen(output_filename, "w") : stdout;

	/*** COMPILE STUFF ***/
#if 0

	/* Parse each file that were given as input. */
	for(string filename : files_list) {
		ClassCompile compiler (filename);
		compiler.compileFile();
		std::cout << "[DONE] Parsing: " << filename << std::endl;
	}
	
	/*** CLEAN UP ***/
	
	/* Destroy parser. */
	extern int yylex_destroy();
	int result_yylex_destroy = yylex_destroy();
	if (result_yylex_destroy != 0) {
		printf("ERROR: Could not destroy yylex. %d\n", result_yylex_destroy);
	}
#endif

	return 0;
}
