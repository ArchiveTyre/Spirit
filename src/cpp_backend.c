#include "cpp_backend.h"
#include <stdio.h>
#include <string.h>

/**
 * Compiles a symbol table recursively into C++ code.
 */
static void compile_sym_to_cpp(SymbolTableEntry *sym, FILE *out, int indent)
{
	#ifdef DEBUG
			for(int i = 0; i < indent; i++)
				putchar('\t');
	#endif

	if (strcmp("class", sym->symbol_type) == 0) {
		fprintf(out, "class %s", sym->symbol_name);
		if (sym->first_child == NULL)
			fputs("{};\n", out);
	}
	else if (strcmp("fun", sym->symbol_type) == 0) {
		fprintf(out, "void *%s();\n", sym->symbol_name);
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
			compile_ast_to_cpp(ast->args_chain, out, true, 0);
		fputs(")", out);
	}
	else if (strcmp(ast->name, "else") == 0) {
		fputs("else", out);
	}
	else if (strcmp(ast->name, "fun") == 0) {
		fprintf(out, "void %s::*%s()", ast->parent_node->symentry->symbol_name, ast->args_chain->name);
	}
	else {
		printf("ERROR: Unknown block used: %s\n", ast->name);
	}
}

/**
 * Compiles any AST node into AST code.
 */
void compile_ast_to_cpp(ASTNode *ast, FILE* out, bool in_expr, int indent_with)
{
	if (!in_expr)
		for(int i = 1; i < indent_with; i++)
			putchar('\t');

	switch (ast->ast_type) {
		case AST_DEFAULT_ARG:
			fputs("default", out);
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
			fprintf(out, "%s(", ast->name);
			compile_ast_to_cpp(ast->args_chain, out, true, 0);
			fprintf(out, ")");
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

	if (ast->args_next != NULL) {
		fputs(", ", out);
		compile_ast_to_cpp(ast->args_next, out, true, 0);
	}

	if (ast->body_first_child != NULL) {
		if (ast->parent_node != NULL)
			fputs(" {\n", out);

		compile_ast_to_cpp(ast->body_first_child, out, false, indent_with + 1);
		if (ast->parent_node != NULL) {
			for(int i = 1; i < indent_with; i++)
				putchar('\t');
			fputs("}\n", out);
		}
	}

	/* If we don't have sub-nodes. We need at newline. But only if not in expr. */
	else if (!in_expr)
		fputc('\n', out);

	if (ast->body_next_sibling != NULL) {
		compile_ast_to_cpp(ast->body_next_sibling, out, false, indent_with);
	}
}
