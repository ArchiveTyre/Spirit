#pragma once

typedef enum {
	AST_NONE,
	AST_SYMBOL,
	AST_FUNCTION_CALL,
	AST_FUNCTION_ARG,
	AST_NUMBER,
	AST_STRING,
	AST_BLOCK,
	AST_DEFAULT_ARG
} EAstType;

typedef struct ASTNode ASTNode;
struct ASTNode {

	/* Genreral stuff. */
	EAstType ast_type;
	int indent_level;
	ASTNode *body_next_sibling;
	ASTNode *body_first_child;
	ASTNode *args_next;
	/* This can be used by: AST_OPERATION, AST_SYMBOL, AST_FUNCTION_CALL, AST_BLOCK. */
	char *name;

	/* For AST_BLOCK. */
	ASTNode *parent_node;

	/* For AST_FUNCTION_CALL and AST_BLOCK. */
	ASTNode *args_chain;

	/* For AST_NUMBER. */
	int number_value;

	/* For AST_STRING*/
	char *string_value;


};

ASTNode *ast_root_node;
ASTNode *ast_prev_node;

/**
 * This function automatically places a statement nodes or a block nodes in the
 * right places.
 */
void ast_insert_node(ASTNode *node);

void debug_ast_node(ASTNode *node, int indent);
void free_ast_node(ASTNode *target);

ASTNode *ast_make_func_call(char *function_name);

/**
 * Creates a number.
 * Example... Well, you don't really
 * need one, but because I'm feeling nice today: 77777777
 */
ASTNode *ast_make_number(int value);

/**
 * Creates a symbol.
 * Symbols can be seen as variables for the most part.
 * Example: var
 */
ASTNode *ast_make_symbol(char *symbol);

/**
 * Creates a string.
 * Example: "You're only resorting to physical abuse because you can't prove that I'm wrong. ~ Armin"
 */
ASTNode *ast_make_string(char *value);

/**
 * Makes an operator. Which is more like an infix function call.
 * Examples: +, -, /, *, etc.
 */
ASTNode *ast_make_op(char *op, ASTNode *l, ASTNode *r);

/**
 * A block statement.
 * Example: if, while, for, switch, etc.
 */
ASTNode *ast_make_block(char *block_type);

/**
 * Creates a new root node. This also sets ast_prev_node as
 * the root node is always the first node.
 * There can be nothing before the root node.
 */
ASTNode *ast_make_root();

/**
 * Default args should be replaces at compile time.
 */
ASTNode *ast_make_default_arg();
