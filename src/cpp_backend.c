#include "cpp_backend.h"
#include <stdio.h>
#include <string.h>

/**
 * Compiles a symbol table recursively into C++ code.
 */
static void compile_sym_to_cpp(SymbolTableEntry *sym, FILE *out)
{
	if (strcmp("class", sym->symbol_type) == 0) {
		fprintf(out, "class %s {\n", sym->symbol_name);
		fprintf(out, "};\n");
	}
	else if (strcmp("fun", sym->symbol_type) == 0) {
		fprintf(out, "void *%s();\n", sym->symbol_name);
	}
	else {
		printf("ERROR: Undefined type: %s\n", sym->symbol_type);
	}
	if (sym->first_child != NULL)
		compile_sym_to_cpp(sym->first_child, out);
	if (sym->next_sibling != NULL)
		compile_sym_to_cpp(sym->next_sibling, out);

}

/**
 * Compiles blocks into C++ code.
 */
static void compile_block_to_cpp(ASTNode *ast, FILE* out)
{
	// FIXME: Use a hashmap or something...
	if (strcmp(ast->name, "class") == 0) {
		compile_sym_to_cpp(ast->symentry, out);
		//fprintf(out, "class");
	}
	else if (strcmp(ast->name, "if") == 0) {
		fputs("if (", out);
		if (ast->args_chain != NULL)
			compile_ast_to_cpp(ast->args_chain, out);
		fputs(")\n", out);
	}
	else if (strcmp(ast->name, "fun") == 0) {
		fprintf(out, "void *%s()\n", ast->args_chain->name);
	}
	else {
		printf("ERROR: Unknown block used: %s\n", ast->name);
	}
}

/**
 * Compiles any AST node into AST code.
 */
void compile_ast_to_cpp(ASTNode *ast, FILE* out)
{
	switch (ast->ast_type) {
		case AST_STRING:
			fprintf(out, "\"%s\"", ast->string_value);
			break;
		case AST_FUNCTION_CALL:
			fprintf(out, "%s(", ast->name);
			compile_ast_to_cpp(ast->args_chain, out);
			fprintf(out, ");\n");
			break;
		case AST_BLOCK:
			compile_block_to_cpp(ast, out);
			break;
		case AST_NONE:
		default:
			printf("ERROR: Compiler error in switch number 2!, %d\n", ast->ast_type);
	}
	if (ast->args_next != NULL) {
		compile_ast_to_cpp(ast->args_next, out);
	}
	if (ast->body_first_child != NULL) {
		if (ast->parent_node != NULL)
			fputs("{\n", out);
#ifdef DEBUG
		for(int i = 0; i <= ast->indent_level; i++)
			putchar('\t');
#endif
		compile_ast_to_cpp(ast->body_first_child, out);
		if (ast->parent_node != NULL)
			fputs("}\n", out);
	}
	if (ast->body_next_sibling != NULL) {
#ifdef DEBUG
		for(int i = 0; i <= ast->indent_level; i++)
			putchar('\t');
#endif
		compile_ast_to_cpp(ast->body_next_sibling, out);
	}
}
