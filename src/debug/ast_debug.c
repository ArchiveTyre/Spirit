#include "ast_debug.h"
#include <stdlib.h>
#include <stdio.h>

void debug_ast_node(ASTNode *node, bool in_expr, bool do_next, int indent)
{

	/* Check for null. */
	if (node == NULL) {
		printf("NULL");
		return;
	}

	/* Print line number and indentaion. */
	if (!in_expr)
		printf("L:%03d,i%01d: ", node->line_no, node->indent_level);

	/* Indent. */
	if (indent != -1)
		for(int i = 0; i < indent; i++)
			putchar('\t');


	if (node->ast_type == AST_TYPE_SPECIFIER) {
		printf("->");
	}

	if (node->ast_type == AST_VAR_DEF) {
		printf("var %s ", node->args_chain->name);
	}

	if (node->ast_type == AST_INLINE) {
		printf("inline (");
	}

	if (node->name != NULL)
		printf("%s", node->name);

	switch(node->ast_type) {
		case AST_DEFAULT_ARG:
			printf("default");
			break;

		case AST_INLINE:
			printf(")");
			break;
		case AST_FUNCTION_DEF:
			debug_ast_node(node->args_chain, true, true, -1);
			printf(":");
			break;

		case AST_VAR_DEF:
			break;
		case AST_TYPE_SPECIFIER:
			break;

		case AST_TUPLE:
			printf("(");
			debug_ast_node(node->args_chain, true, true, -1);
			printf(")");
			break;
		case AST_FUNCTION_CALL:
			debug_ast_node(node->args_chain, true, false, -1);
			printf("(");
			debug_ast_node(node->args_chain->args_next, true, true, -1);
			printf(")");
			break;
		case AST_BLOCK:
			printf(" ");
			debug_ast_node(node->args_chain, true, true, -1);
			putchar(':');
			break;
		case AST_SYMBOL:
			break;
		case AST_NUMBER:
			printf("%d", node->number_value);
			break;
		case AST_STRING:
			printf("\"%s\"", node->string_value);
			break;
		default:
			printf("ERROR: Compiler error switch number 0!, %d\n", node->ast_type);
	}

	if (do_next && node->args_next != NULL) {
		printf(", ");
		debug_ast_node(node->args_next, true, true, -1);
	}

	if (indent != -1)
		putchar('\n');
	if (node->body_first_child != NULL)
		debug_ast_node(node->body_first_child, false, true, indent+1);
	if (node->body_next_sibling != NULL)
		debug_ast_node(node->body_next_sibling, false, true, indent);
}
