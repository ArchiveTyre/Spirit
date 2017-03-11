#include "cpp_backend.h"
#include <stdio.h>
#include <string.h>
#include <assert.h>

/**
 * Tries to compile a special case function.
 * If we do any compilation we return true.
 */
static bool compile_special_sym_function(SymbolTableEntry *sym, FILE *out)
{
	if (strcmp(sym->symbol_name, "construct") == 0) {
		// FIXME: Access modifiers must not look like this.
		fprintf(out, "public: %s();\n", sym->parent_table->symbol_name);
		return true;
	}
	return false;
}

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
		if (sym->first_child == NULL)
			fputs("{};\n", out);
	}
	else if (strcmp("fun", sym->symbol_type) == 0) {
		if (!compile_special_sym_function(sym, out)) {

			assert(sym->symbol_info != NULL);

			char* return_type = sym->symbol_info->info_text;
			fprintf(out, "%s %s();\n", return_type, sym->symbol_name);
		}
	}
	else {
		printf("ERROR: Undefined type: %s\n", sym->symbol_type);
	}
	if (sym->first_child != NULL) {
		fprintf(out, " {\n");
		compile_sym_to_cpp(sym->first_child, out, indent + 1);
		fprintf(out, "};\n");
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

		/* Override special function "construct. ". */
		if (strcmp(ast->args_chain->name, "construct") == 0) {
			char *class_name = ast->parent_node->args_chain->string_value;
			fprintf(out, "%s::%s()", class_name, class_name);
		}

		/* Normal function declaration. */
		else {

			char *return_type = ast->symentry->symbol_info->info_text;
			char *class_name = ast->parent_node->symentry->symbol_name;
			char *function_name = ast->args_chain->name;

			fprintf(out, "%s %s::%s()", return_type, class_name, function_name);
		}
	}
	else {
		printf("ERROR: Unknown block used: %s\n", ast->name);
	}
}

static bool compile_special_function(ASTNode *ast, FILE *out, bool in_expr) {
	if (strcmp(ast->name, "=") == 0) {
		compile_ast_to_cpp(ast->args_chain, out, true, true, 0);
		fprintf(out, " = ");
		compile_ast_to_cpp(ast->args_chain->args_next, out, true, true, 0);
		return true;
	}
	else if (strcmp(ast->name, "") == 0) {
		return true;
	}
	return false;
}

/**
 * Compiles any AST node into AST code.
 */
void compile_ast_to_cpp(ASTNode *ast, FILE* out, bool in_expr, bool skip_siblings, int indent_with)
{
	if (!in_expr)
		for(int i = 1; i < indent_with; i++)
			fputc('\t', out);

	switch (ast->ast_type) {
		case AST_DEFAULT_ARG:
			fputs("/* Default. */", out);
			break;
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

			/* If we fail to compile a function the "special" way. We do it the normal way. */
			if (!compile_special_function(ast, out, in_expr)) {
				fprintf(out, "%s(", ast->name);
				compile_ast_to_cpp(ast->args_chain, out, true, false, 0);
				fprintf(out, ")");
			}
			if (!in_expr)
				fputc(';', out);

			break;
		case AST_BLOCK:
			compile_block_to_cpp(ast, out);
			break;
		case AST_NONE:
		default:
			printf("ERROR: Compiler error in switch number 2!, %d\n", ast->ast_type);
	}

	if (ast->body_first_child != NULL) {
		if (ast->parent_node != NULL)
			fputs(" {\n", out);

		compile_ast_to_cpp(ast->body_first_child, out, false, false, indent_with + 1);
		if (ast->parent_node != NULL) {
			for(int i = 1; i < indent_with; i++)
				fputc('\t', out);
			fputs("}\n", out);
		}
	}

	/* If we don't have sub-nodes. We need at newline. But only if not in expr. */
	else if (!in_expr)
		fputc('\n', out);

	if (skip_siblings == false) {
		if (ast->body_next_sibling != NULL) {
			compile_ast_to_cpp(ast->body_next_sibling, out, false, false, indent_with);
		}

		if (ast->args_next != NULL) {
			fputs(", ", out);
			compile_ast_to_cpp(ast->args_next, out, true, false, 0);
		}
	}
}
