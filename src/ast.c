#include "ast.h"
#include "stdlib.h"
#include "stdio.h"
#include "string.h"

void debug_ast_node(ASTNode *node, int indent)
{
	if (node == NULL) {
		printf("null");
		return;
	}
	if (indent != -1)
		for(int i = 0; i < indent; i++)
			putchar('\t');

	if (node->name != NULL)
		printf("%s", node->name);

	switch(node->ast_type) {
		case AST_DEFAULT_ARG:
			printf("default");
			break;
		case AST_FUNCTION_CALL:
			printf("(");
			debug_ast_node(node->args_chain, -1);
			printf(")");
			break;
		case AST_BLOCK:
			printf(" ");
			debug_ast_node(node->args_chain, -1);
			putchar(':');
			break;
		case AST_SYMBOL:
			break;
		case AST_NUMBER:
			printf("%d", node->number_value);
			break;
		case AST_OPERATION:
			printf("(");
			debug_ast_node(node->left_ast, -1);
			printf(", ");
			debug_ast_node(node->right_ast, -1);
			printf(")");
			break;
		case AST_STRING:
			printf("\"%s\"", node->string_value);
			break;
		default:
			printf("ERROR: Compiler error (0) in switch!, %d", node->ast_type);
	}

	if (node->args_next != NULL) {
		printf(", ");
		debug_ast_node(node->args_next, -1);
	}

	if (indent != -1)
		putchar('\n');
	if (node->body_first_child != NULL)
		debug_ast_node(node->body_first_child, indent+1);
	if (node->body_next_sibling != NULL)
		debug_ast_node(node->body_next_sibling, indent);
}

void free_ast_node(ASTNode *target)
{
	switch(target->ast_type) {
		case AST_BLOCK:
			break;
		case AST_STRING:
			free(target->string_value);
			break;
		case AST_SYMBOL:
			break;
		case AST_OPERATION:
			free_ast_node(target->left_ast);
			free_ast_node(target->right_ast);
			break;
		case AST_FUNCTION_CALL:
			break;
		case AST_NONE:
		default:
			printf("ERROR: Compiler error (1) in switch!");
	}
	if (target->name != NULL)
		free(target->name);
	if (target->args_chain != NULL)
		free(target->args_chain);
	if (target->args_next != NULL)
		free_ast_node(target->args_next);
	if (target->body_next_sibling != NULL)
		free_ast_node(target->body_next_sibling);
	if (target->body_first_child != NULL)
		free_ast_node(target->body_first_child);
	free(target);
}

extern int line_indent;

void ast_insert_node(ASTNode *node)
{
#ifdef DEBUG
	printf("Inserting at indent: %d\n", line_indent);
#endif

	/* Descend. */
	if (node->indent_level > ast_prev_node->indent_level) {
		ASTNode **dest = &ast_prev_node->body_first_child;
		while (*dest != NULL)
			dest = &(*dest)->body_next_sibling;
		*dest = node;
		node->parent_node = ast_prev_node;
	}

	/* Stay. */
	else if (ast_prev_node->indent_level == node->indent_level) {
		ast_prev_node->body_next_sibling = node;
		node->parent_node = ast_prev_node->parent_node;

	}

	/* Ascend. */
	else {

		/* Find the correct depth. */
		ASTNode *parent = ast_prev_node->parent_node;
		while (parent->indent_level >= node->indent_level)
			parent = parent->parent_node;

		/* Now add the node to the found result. */
		ASTNode **dest = &parent->body_first_child;
		while (*dest != NULL)
			dest = &(*dest)->body_next_sibling;
		*dest = node;
		node->parent_node = parent;
	}
	ast_prev_node = node;
	line_indent = 0;
}

static ASTNode *init_ast_node(EAstType type)
{
	ASTNode *target = malloc(sizeof(ASTNode));
	target->ast_type = type;
	target->indent_level = line_indent;
	target->args_next = NULL;
	target->args_chain = NULL;
	target->body_next_sibling = NULL;
	target->body_first_child = NULL;
	target->name = NULL;
	return target;
}

ASTNode *ast_make_root()
{
	ASTNode *target = init_ast_node(AST_BLOCK);
	target->name = strdup("root");
	target->indent_level = -1;

	/* When a root node is created it's always the newest one. */
	ast_prev_node = target;
	return target;
}

ASTNode *ast_make_block(char *block_type)
{
#ifdef DEBUG
	printf("Making block of type: %s\n", block_type);
#endif
	ASTNode *target = init_ast_node(AST_BLOCK);
	target->name = strdup(block_type);
	return target;
}

ASTNode *ast_make_default_arg()
{
	ASTNode *target = init_ast_node(AST_DEFAULT_ARG);
	return target;
}

ASTNode *ast_make_func_call(char *function_name)
{
#ifdef DEBUG
	printf("Function call to: %s\n", function_name);
#endif
	ASTNode *target = init_ast_node(AST_FUNCTION_CALL);
	target->name = strdup(function_name);
	return target;
}

ASTNode *ast_make_number(int value)
{
	printf("Creating number: %d\n", value);
	ASTNode *target = init_ast_node(AST_NUMBER);
	target->number_value = value;
	return target;
}

ASTNode *ast_make_string(char *value)
{
	ASTNode *target = init_ast_node(AST_STRING);
	target->string_value = strdup(value);
	return target;
}

ASTNode *ast_make_op(char *op, ASTNode *l, ASTNode *r)
{
#ifdef DEBUG
	printf("Creating OP: %s\n", op);
#endif
	ASTNode *target = init_ast_node(AST_OPERATION);
	target->name = strdup(op);
	target->left_ast = l;
	target->right_ast = r;
	return target;
}

ASTNode *ast_make_symbol(char *symbol_name)
{
	ASTNode *target = init_ast_node(AST_SYMBOL);
	target->name = strdup(symbol_name);
	return target;
}
