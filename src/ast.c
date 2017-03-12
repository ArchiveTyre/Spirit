#include "ast.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

extern int line_indent;

void debug_ast_node(ASTNode *node, bool in_expr,  int indent)
{
	if (node == NULL) {
		printf("null");
		return;
	}

	if (!in_expr)
		printf("L:%03d,i%01d: ", node->line_no, node->indent_level);

	if (indent != -1)
		for(int i = 0; i < indent; i++)
			putchar('\t');

	if (node->ast_type == AST_TYPE_SPECIFIER) {
		printf("->");
	}

	if (node->ast_type == AST_VAR_DEF) {
		printf("var %s ", node->args_chain->name);
	}

	if (node->name != NULL)
		printf("%s", node->name);

	switch(node->ast_type) {
		case AST_DEFAULT_ARG:
			printf("default");
			break;

		case AST_VAR_DEF:
			break;

		case AST_TYPE_SPECIFIER:
			break;

		case AST_TUPLE:
		case AST_FUNCTION_CALL:
			printf("(");
			debug_ast_node(node->args_chain, true, -1);
			printf(")");
			break;
		case AST_BLOCK:
			printf(" ");
			debug_ast_node(node->args_chain, true, -1);
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

	if (node->args_next != NULL) {
		printf(", ");
		debug_ast_node(node->args_next, true, -1);
	}

	if (indent != -1)
		putchar('\n');
	if (node->body_first_child != NULL)
		debug_ast_node(node->body_first_child, false, indent+1);
	if (node->body_next_sibling != NULL)
		debug_ast_node(node->body_next_sibling, false, indent);
}

void ast_auto_insert_node(ASTNode *node)
{
#ifdef DEBUG
	printf("Inserting at indent: %d\n", line_indent);
#endif

	/* Descend. */
	if (node->indent_level > ast_prev_node->indent_level) {
		ast_insert_child_node(ast_prev_node, node);
		/*ASTNode **dest = &ast_prev_node->body_first_child;
		while (*dest != NULL)
			dest = &(*dest)->body_next_sibling;
		*dest = node;
		node->parent_node = ast_prev_node;*/
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
		ast_insert_child_node(parent, node);
	}
	ast_prev_node = node;
	line_indent = 0;
}

void ast_insert_child_node(ASTNode *parent_node, ASTNode *child_node)
{
	ASTNode **dest = &parent_node->body_first_child;

	/* Find an empty reference to link child_node into. */
	while (*dest != NULL)
		dest = &(*dest)->body_next_sibling;
	*dest = child_node;
	child_node->parent_node = parent_node;
}

void ast_insert_arg(ASTNode *target_node, ASTNode *target_arg)
{
	ASTNode **dest = &target_node->args_chain;

	/* Find an empty reference to link target_arg into. */
	while (*dest != NULL)
		dest = &(*dest)->args_next;
	*dest = target_arg;
	target_arg->parent_node = target_node;
}

void ast_make_sym_tree(ASTNode *node)
{
	if (node->parent_node == NULL && node != ast_root_node) {
		printf("ERROR: Compiler error: Node has no parent.\n");
		abort();
		return;
	}

	/* If this node has already created a symbol then we don't need to crate it. */
	if (node->symentry == NULL)
	{
		switch(node->ast_type)
		{
			case AST_BLOCK:
				if (strcmp(node->name, "class") == 0) {
					node->symentry = sym_define(node->args_chain->string_value,
						node->name, NULL);
				}
				else if(strcmp(node->name, "fun") == 0) {
					node->symentry = sym_define(node->args_chain->name,
						node->name, node->parent_node->symentry);

					/* The return type is the second argument. */
					char *return_type = node->args_chain->args_next->args_chain->name;
					sym_add_info(node->symentry, return_type);

					ASTNode *first_arg = NULL;
					if (node->args_chain->args_next->args_next != NULL)
						first_arg = node->args_chain->args_next->args_next->args_chain;
					while (first_arg != NULL) {
						sym_add_info(node->symentry, first_arg->args_chain->name);
						sym_add_info(node->symentry, " ");
						sym_add_info(node->symentry, first_arg->name);
						if (first_arg->args_next != NULL)
							sym_add_info(node->symentry, ", ");

						first_arg = first_arg->args_next;
					}
				}
				else if (strcmp(node->name, "if") == 0) {
					node->symentry = sym_define("", "fun", node->parent_node->symentry);
				}
				else if (strcmp(node->name, "else") == 0) {
					node->symentry = sym_define("", "else", node->parent_node->symentry);
				}

				// FIXME: This error is reported else where anyway.
				// We could just remove this so that we don't have any empty blocks.
				else {
					printf("ERROR: Unknown block used: %s\n", node->name);
				}
				break;
			case AST_VAR_DEF:
			{
				char *var_type = node->args_chain->name;
				char *var_name = node->name;
				node->symentry = sym_define(var_name, "var", node->parent_node->symentry);
				sym_add_info(node->symentry, var_type);
			}
			break;
			case AST_TUPLE:
				node->symentry = sym_define("", "tuple", node->parent_node->symentry);
				break;
			case AST_SYMBOL:
				node->symentry = sym_find(node->name, node->parent_node->symentry);
				break;
			case AST_TYPE_SPECIFIER:
				node->symentry = sym_find(node->name, node->parent_node->symentry);
				break;

			/* Forward the symtable. */
			case AST_FUNCTION_CALL:
			case AST_DEFAULT_ARG:
			case AST_NUMBER:
			case AST_STRING:
				node->symentry = node->parent_node->symentry;
				break;
			default:
				printf("ERROR: Compiler error in switch number 3!, %d\n", node->ast_type);
				abort();
		}
	}

	/* However, we might want to make sure that the parent_table is correct. Just in case. */
	else if (node->parent_node != NULL){
		node->symentry->parent_table = node->parent_node->symentry;
	}

	if (node->args_chain != NULL) {
		/* Args are children of the function call. */
		node->args_chain->parent_node = node;
		ast_make_sym_tree(node->args_chain);
	}
	if (node->args_next != NULL) {
		/* Siblings share their parents. (^.^)*/
		node->args_next->parent_node = node->parent_node;
		ast_make_sym_tree(node->args_next);
	}
	if (node->body_first_child != NULL)
		ast_make_sym_tree(node->body_first_child);
	if (node->body_next_sibling != NULL)
		ast_make_sym_tree(node->body_next_sibling);

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

void free_ast_node(ASTNode *target)
{

	/* Specific stuff. */
	if (target->ast_type == AST_STRING)
		free(target->string_value);

	/* Generic stuff. */
	if (target->name != NULL)
		free(target->name);
	if (target->args_chain != NULL)
		free_ast_node(target->args_chain);
	if (target->args_next != NULL)
		free_ast_node(target->args_next);
	if (target->body_next_sibling != NULL)
		free_ast_node(target->body_next_sibling);
	if (target->body_first_child != NULL)
		free_ast_node(target->body_first_child);
	free(target);
}

static ASTNode *init_ast_node(EAstType type)
{
	extern int yylineno;

	ASTNode *target = malloc(sizeof(ASTNode));
	target->ast_type = type;
	target->indent_level = line_indent;
	target->line_no = yylineno;
	target->args_next = NULL;
	target->args_chain = NULL;
	target->body_next_sibling = NULL;
	target->body_first_child = NULL;
	target->name = NULL;
	target->symentry = NULL;
	target->parent_node = NULL;
	return target;
}

ASTNode *ast_make_root(char *class_name)
{
	ASTNode *target = init_ast_node(AST_BLOCK);
	target->name = strdup("class");
	target->args_chain = ast_make_string(class_name);
	target->indent_level = -1;
	//target->symentry = sym_define("root", "class", NULL);

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

ASTNode *ast_make_var_def(char *var_name, char *var_type)
{
#ifdef DEBUG
	printf("Making var: %s of type: %s\n", var_name, var_type);
#endif
	ASTNode *target = init_ast_node(AST_VAR_DEF);
	target->name = strdup(var_name);
	ast_insert_arg(target, ast_make_type_specifier(var_type));
	return target;
}

ASTNode *ast_make_type_specifier(char *type_name)
{
	printf("Type: %s\n", type_name);
	ASTNode *target = init_ast_node(AST_TYPE_SPECIFIER);
	target->name = strdup(type_name);
	return target;
}

ASTNode *ast_make_tuple(ASTNode *chain)
{
	ASTNode *target = init_ast_node(AST_TUPLE);
	target->args_chain = chain;

	// FIXME: Iterate through all chain links.
	chain->parent_node = chain;
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

ASTNode *ast_make_default_arg()
{
	ASTNode *target = init_ast_node(AST_DEFAULT_ARG);
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

ASTNode *ast_make_symbol(char *symbol_name)
{
	ASTNode *target = init_ast_node(AST_SYMBOL);
	target->name = strdup(symbol_name);
	return target;
}

ASTNode *ast_make_op(char *op, ASTNode *l, ASTNode *r)
{
#ifdef DEBUG
	printf("Creating OP: %s\n", op);
#endif
	ASTNode *target = init_ast_node(AST_FUNCTION_CALL);
	target->name = strdup(op);
	target->args_chain = l;
	l->args_next = r;
	return target;
}
