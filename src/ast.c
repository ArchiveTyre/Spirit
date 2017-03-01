#include "ast.h"
#include "stdlib.h"
#include "stdio.h"
#include "string.h"

void debug_ast_node(ASTNode *node, int indent)
{
	if (indent != -1)
		for(int i = 0; i < indent; i++)
			putchar('\t');

	if (node->args_next != NULL) {
		printf(", ");
		debug_ast_node(node->args_next, -1);
	}
	switch(node->ast_type) {
		case AST_FUNCTION_CALL:
			printf("%s(", node->function_name);
			debug_ast_node(node->args_chain, -1);
			printf(")");
			break;
		case AST_BLOCK:
			printf("%s:", node->block_type);
			break;
		case AST_SYMBOL:
			printf("%s", node->symbol_name);
			break;
		case AST_NUMBER:
			printf("%d", node->number_value);
			break;
		case AST_OPERATION:
			printf("%s(", node->op_name);
			debug_ast_node(node->left_ast, -1);
			printf(", ");
			debug_ast_node(node->right_ast, -1);
			printf(")");
			break;
		case AST_STRING:
			printf("\"%s\"", node->string_value);
			break;
		default:
			printf("ERROR: Compiler error (0) in switch!");
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
			free(target->block_type);
			break;
		case AST_STRING:
			free(target->string_value);
			break;
		case AST_SYMBOL:
			free(target->symbol_name);
			break;
		case AST_OPERATION:
			free(target->op_name);
			free(target->left_ast);
			free(target->right_ast);
			break;
		case AST_FUNCTION_CALL:
			free(target->args_chain);
			free(target->function_name);
			break;

		case AST_NONE:
		default:
			printf("ERROR: Compiler error (1) in switch!");
	}

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
}

static ASTNode *init_ast_node(EAstType type)
{
	printf("Creating node.\n");
	ASTNode *target = malloc(sizeof(ASTNode));
	target->ast_type = type;
	target->args_next = NULL;
	target->body_next_sibling = NULL;
	target->body_first_child = NULL;
	target->indent_level = line_indent;

	return target;
}

ASTNode *ast_make_root()
{
	ASTNode *target = init_ast_node(AST_BLOCK);
	target->block_type = "root";
	target->indent_level = -1;
	return target;
}

ASTNode *ast_make_func_call(char *function_name)
{
	ASTNode *target = init_ast_node(AST_FUNCTION_CALL);
	target->function_name = strdup(function_name);

	printf("Function call to: %s\n", function_name);
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
	printf("Creating OP: %s\n", op);
	ASTNode *target = init_ast_node(AST_OPERATION);
	target->op_name = strdup(op);
	target->left_ast = l;
	target->right_ast = r;
	return target;
}

ASTNode *ast_make_symbol(char *symbol_name)
{
	ASTNode *target = init_ast_node(AST_SYMBOL);
	target->symbol_name = strdup(symbol_name);
	return target;
}
