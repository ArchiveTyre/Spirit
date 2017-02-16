#pragma once

/**
 * This struct contains a basic AST node.
 * Create it using new_ast_node(...);
 */
typedef struct ASTNode ASTNode;
struct ASTNode {
	ASTNode *first_child;
	ASTNode *parent_node;
	ASTNode *next_node;
	int indentation;

	char *args;
	char *command;
};

/**
 * Frees an AST node recursively and anything following it.
 */
void free_ast_node(ASTNode *this);

/**
 * Wrapper function to create a basic AST node.
 */
ASTNode* new_ast_node(char *command, char *args, int indentation);

/**
 * Inserts a node inside of argument 'this'.
 */
void ast_node_insert(ASTNode *this, ASTNode *node);

/**
 * Prints data about an AST node recursively.
 */
void debug_ast_node(ASTNode *this, int indentation);

