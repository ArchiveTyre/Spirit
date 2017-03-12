#include "cpp_backend.h"
#include <stdio.h>
#include <string.h>
#include <assert.h>

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
		if (sym->first_child != NULL) {
			fprintf(out, " {\n");
			compile_sym_to_cpp(sym->first_child, out, indent + 1);
			fprintf(out, "};\n");
		}
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
			while(call_arguments != NULL) {
				fprintf(out, "%s", call_arguments->info_text);
				call_arguments = call_arguments->info_sibling;
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
		case AST_TUPLE:
			break;
		case AST_TYPE_SPECIFIER:
			break;
		case AST_VAR_DEF:
			{
				char *var_type = ast->symentry->symbol_info->info_text;
				char *var_name = ast->symentry->symbol_name;
				fprintf(out, "%s %s", var_type, var_name);
				if (!in_expr)
					fputc(';', out);
			}
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
