#include "cpp_backend.h"
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include "compile.h"

/**
 * Compiles a symbol table recursively into C++ code.
 */
static void compile_sym_to_cpp(SymbolTableEntry *sym, FILE *out, int indent)
{
	#ifdef DEBUG
			for(int i = 0; i < indent; i++)
				fputc('\t', out);
	#endif

	if (strcmp("class", sym->symbol_type) == 0) {
		fprintf(out, "class %s", sym->symbol_name);

		/* Compile sub syms. */
		if (sym->first_child != NULL) {
			fprintf(out, " {\n");
			compile_sym_to_cpp(sym->first_child, out, indent + 1);
			fprintf(out, "};\n");
		}

		/* In the rare case that the class is empty. */
		else {
			fputs("{};\n", out);
		}
	}
	else if (strcmp("fun", sym->symbol_type) == 0) {
			assert(sym->symbol_info != NULL);

			char *return_type;
			char *function_name;
			SymbolInfo *call_arguments = sym->symbol_info->info_sibling;

			if (strcmp(sym->symbol_name, "construct") == 0) {

				// FIXME: This is a hack, should be moved to access modifier.
				return_type = "public:";
				function_name = sym->parent_table->symbol_name;
			}
			else {
				return_type = sym->symbol_info->info_text;
				function_name = sym->symbol_name;
			}

			fprintf(out, "%s %s(", return_type, function_name);
			if (call_arguments != NULL) {

				/* The remaining arguments are types and names. */
				fprintf(out, "%s %s", call_arguments->info_text, call_arguments->info_sibling->info_text);

				/* We read two args at the same time so we skip two args too. */
				call_arguments = call_arguments->info_sibling->info_sibling;

				while (call_arguments != NULL) {

					/* current and current + 1 make up one vairable definition. */
					fprintf(out, ", %s %s", call_arguments->info_text, call_arguments->info_sibling->info_text);

					/* We read two args at the same time so we skip two args too. */
					call_arguments = call_arguments->info_sibling->info_sibling;
				}
			}
			fputs(");\n", out);

	}
	else if (strcmp("var", sym->symbol_type) == 0) {

		if (strcmp(sym->parent_table->symbol_type, "tuple") != 0) {
			char *var_type = sym->symbol_info->info_text;
			char *var_name = sym->symbol_name;
			fprintf(out, "%s %s;\n", var_type, var_name);
		}
	}
	else if (strcmp("tuple", sym->symbol_type) == 0) {

	}
	else {
		printf("ERROR: Undefined type: %s\n", sym->symbol_type);
	}
	if (sym->next_sibling != NULL)
		compile_sym_to_cpp(sym->next_sibling, out, indent);

}

/**
 * Compiles blocks into C++ code.
 */
static void compile_block_to_cpp(ASTNode *ast, FILE* out)
{
	// FIXME: Use a hashmap or something...
	if (strcmp(ast->name, "class") == 0) {
		compile_sym_to_cpp(ast->symentry, out, 0);
	}
	else if (strcmp(ast->name, "if") == 0) {
		fputs("if (", out);
		if (ast->args_chain != NULL)
			compile_ast_to_cpp(ast->args_chain, out, true, false, 0);
		fputs(")", out);
	}
	else if (strcmp(ast->name, "else") == 0) {
		fputs("else", out);
	}
	else if (strcmp(ast->name, "fun") == 0) {

		char *function_name;
		char *return_type;
		char *class_name = ast->parent_node->symentry->symbol_name;
		ASTNode *first_arg = NULL;

		/* Override special function "construct. ". */
		if (strcmp(ast->args_chain->name, "construct") == 0) {
			return_type = "";
			function_name = class_name;
		}
		else {
			return_type = ast->symentry->symbol_info->info_text;
			function_name = ast->args_chain->name;
		}

		if (ast->args_chain->args_next->args_next != NULL)
			first_arg = ast->args_chain->args_next->args_next->args_chain;

		fprintf(out, "%s %s::%s (", return_type, class_name, function_name);
		if (first_arg != NULL)
			compile_ast_to_cpp(first_arg, out, true, false, -1);
		fputc(')', out);
	}
	else {
		printf("ERROR: Unknown block used: %s\n", ast->name);
	}
}

/**
 * Compiles any AST node into AST code.
 */
void compile_ast_to_cpp(ASTNode *ast, FILE* out, bool in_expr, bool do_next, int indent_with)
{
#ifdef DEBUG
	if (!in_expr)
		for(int i = 1; i < indent_with; i++)
			fputc('\t', out);
#endif

	switch (ast->ast_type) {
		case AST_DEFAULT_ARG:
			// FIXME: Replace with actual arg.
			fputs("/* Default. */", out);
			break;
		case AST_FUNCTION_DEF:
		{
			char *function_name;
			char *return_type;
			char *class_name = ast->parent_node->symentry->symbol_name;
			ASTNode *first_arg = NULL;

			/* Override special function "construct. ". */
			if (strcmp(ast->name, "construct") == 0) {
				return_type = "";
				function_name = class_name;
			}
			else {
				return_type = ast->symentry->symbol_info->info_text;
				function_name = ast->name;
			}

			if (ast->args_chain->args_next != NULL)
				first_arg = ast->args_chain->args_next->args_chain;

			fprintf(out, "%s %s::%s (", return_type, class_name, function_name);
			if (first_arg != NULL)
				compile_ast_to_cpp(first_arg, out, true, true, 0);
			fputc(')', out);
			break;
		}
		case AST_SYMBOL:
			// FIXME: Support name mangeling.
			fprintf(out, "%s", ast->name);
			break;
		case AST_NUMBER:
			// FIXME: Support more numbers than just int
			fprintf(out, "%d", ast->number_value);
			break;
		case AST_STRING:
			fprintf(out, "\"%s\"", ast->string_value);
			break;
		case AST_FUNCTION_CALL:
			/* In infix the function name is in the middle of the args. */
			if (ast->is_infix) {
				compile_ast_to_cpp(ast->args_chain->args_next, out, true, false, 0);
				fputc (' ', out);
				compile_ast_to_cpp(ast->args_chain, out, true, false, 0);
				fputc (' ', out);
				compile_ast_to_cpp(ast->args_chain->args_next->args_next, out, true, false, 0);
			}

			/* Compile function call the normal way. */
			else {
				compile_ast_to_cpp(ast->args_chain, out, true, false, 0);
				fputc('(', out);
				compile_ast_to_cpp(ast->args_chain->args_next, out, true, true, 0);
				fputc(')', out);
			}
			if (!in_expr)
				fputc(';', out);

			break;
		case AST_BLOCK:
			compile_block_to_cpp(ast, out);
			break;
		case AST_VAR_DEF:
			{
				char *var_type = ast->symentry->symbol_info->info_text;
				char *var_name = ast->symentry->symbol_name;
				fprintf(out, "%s %s", var_type, var_name);

				if (ast->args_chain->args_next != NULL) {
					fputs(" = ", out);
					compile_ast_to_cpp(ast->args_chain->args_next, out, true, true, 0);
				}
				if (!in_expr)
					fputc(';', out);
			}
			break;
		case AST_INLINE:
			fprintf(out, "/* INLINE */ %s /* ENDINLINE */", ast->name);
			break;
		case AST_TUPLE:
		case AST_TYPE_SPECIFIER:
		case AST_NONE:
		default:
			printf("ERROR: Compiler error in switch number 2!, %d\n", ast->ast_type);
	}

	/* Print child nodes. */
	if (ast->body_first_child != NULL) {

		/* Skip extra braces for root nodes. */
		if (ast != current_compile_result->ast_root_node)
			fputs(" {\n", out);

		/* Do the compilation. Nodes indent themselves. */
		compile_ast_to_cpp(ast->body_first_child, out, false, true, indent_with + 1);
		if (ast != current_compile_result->ast_root_node) {
			/* Indent and add extra braces. */
#ifdef DEBUG
			for(int i = 1; i < indent_with; i++)
				fputc('\t', out);
#endif
			fputs("}\n", out);
		}
	}

	/* If we don't have sub-nodes. We need at newline. But only if not in expr. */
	else if (!in_expr)
		fputc('\n', out);

	/* Only if we are allowed to compile the siblings. */
	if (do_next) {

		/* Try to compile sibling. */
		if (ast->body_next_sibling != NULL) {
			compile_ast_to_cpp(ast->body_next_sibling, out, false, true, indent_with);
		}

		/* Try to compile next arg. */
		if (ast->args_next != NULL) {
			fputs(", ", out);
			compile_ast_to_cpp(ast->args_next, out, true, true, 0);
		}
	}
}
