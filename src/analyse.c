#include "analyse.h"
#include <string.h>
#include <stdlib.h>

static char *import_to_path(ASTNode *node, char path_separator) {
	if (AST_SYMBOL == node->ast_type)
		return strdup(node->name);
	else if (AST_FUNCTION_CALL == node->ast_type && strcmp(node->args_chain->name, ".") == 0) {
		char *p1 = import_to_path(node->args_chain->args_next, path_separator);
		char *p2 = import_to_path(node->args_chain->args_next->args_next, path_separator);

		int p1_len = strlen(p1);
		int p2_len = strlen(p2);
		int cat_size = p1_len + p2_len + 1 + 1;

		char *cat = malloc(cat_size);
		strcpy(cat, p1);
		cat[p1_len] = path_separator;
		strcpy(cat + p1_len + 1, p2);
		cat[cat_size] = 0;

		free(p1);
		free(p2);

		return cat;
	}
	else {
		printf("ERROR: On line %d, couldn't parse import.\n", node->line_no);
		abort();
		return NULL;
	}
}

static void resolve_import(CompileResult *class_in)
{
	ASTNode *begin_search = class_in->ast_root_node->body_first_child;
	while (begin_search != NULL) {

		if (AST_FUNCTION_CALL == begin_search->ast_type
			&& strcmp(begin_search->args_chain->name, "import") == 0) {
			printf("Found import...\n");

			/* Set it to nothing to prevent it from being compiled as a function. */
			begin_search->ast_type = AST_NONE;

			// FIXME Compile and find. Not just find.
			ASTNode *import_path = begin_search->args_chain->args_next;
			char *path = import_to_path(import_path, '/');
			printf("Import path: %s\n", path);
			//CompileResult *other = compile_file(path);
			free(path);

		}

		begin_search = begin_search->body_next_sibling;
	}
}

void analyse_and_fix_class(CompileResult *class_in)
{
	resolve_import(class_in);
}
