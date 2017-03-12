#pragma once
#include "symtbl.h"
#include <stdbool.h>

typedef enum {
	AST_NONE,

	/* A block usually has child nodes. */
	AST_BLOCK,

	/* Default args are replaced at compile time. */
	AST_DEFAULT_ARG,

	/*FIXME: Is this even needed? */
	AST_FUNCTION_ARG,

	/* Function call. Name = what to call, first arg = arguments. */
	AST_FUNCTION_CALL,

	/* A simple number. */
	AST_NUMBER,

	/* A simple string. */
	AST_STRING,

	/* A simple variable name. */
	AST_SYMBOL,

	/* Contains multiple variables. */
	AST_TUPLE,

	/* Contains a reference to a type. */
	AST_TYPE_SPECIFIER,

	/* Name = variable name, first arg = type, (optional) second arg = init. */
	AST_VAR_DEF,
} EAstType;

typedef struct ASTNode ASTNode;
struct ASTNode {

	/* Genreral stuff. */
	EAstType ast_type;
	int indent_level;
	int line_no;
	ASTNode *body_next_sibling;
	ASTNode *body_first_child;
	ASTNode *args_next;
	SymbolTableEntry *symentry;
	/* This can be used by: AST_OPERATION, AST_SYMBOL, AST_FUNCTION_CALL, AST_BLOCK. */
	char *name;
	/* Used by expressions and AST_BLOCK. But also set in the ast_make_sym_tree. */
	ASTNode *parent_node;

	/* For AST_FUNCTION_CALL and AST_BLOCK. */
	ASTNode *args_chain;

	/* For AST_NUMBER. */
	int number_value;

	/* For AST_STRING. */
	// FIXME: Use "name" instead.
	char *string_value;


};

ASTNode *ast_root_node;
ASTNode *ast_prev_node;

/**
 * Gives a symbol entry to all child nodes.
 */
void ast_make_sym_tree(ASTNode *node);

/**
 * This function automatically places a statement nodes or a block nodes in the
 * right places.
 */
void ast_auto_insert_node(ASTNode *node);

void ast_insert_child_node(ASTNode *target_node, ASTNode *child_node);
void ast_insert_arg(ASTNode *target_node, ASTNode *target_arg);

/**
 * Prints out the AST given in a tree format.
 */
void debug_ast_node(ASTNode *node, bool in_expr, int indent);

/**
 * Frees the target node along with it's siblings, children
 * and function call chain.
 */
void free_ast_node(ASTNode *target);

/**
 * Creates a new root node. This also sets ast_prev_node as
 * the root node is always the first node.
 * There can be nothing before the root node.
 */
ASTNode *ast_make_root();

/**
 * A block statement.
 * Example: if, while, for, switch, etc.
 */
ASTNode *ast_make_block(char *block_type);

/**
 * Creates a variable definiton.
 * Example: var
 */
ASTNode *ast_make_var_def(char *var_name, char *var_type);

/**
 * Creates a type specifier.
 */
ASTNode *ast_make_type_specifier(char *type_name);

/**
 * Creates a tuple.
 * Example: FIXME
 */
 ASTNode *ast_make_tuple(ASTNode *chain);

/**
 * Creates a function call. Function arguments are added
 * later on.
 * Example: FIXME
 */
ASTNode *ast_make_func_call(char *function_name);

/**
 * Default args should be replaced at compile time.
 */
ASTNode *ast_make_default_arg();

/**
 * Creates a number.
 * Example... Well, you don't really
 * need one, but because I'm feeling nice today: 77777777
 */
ASTNode *ast_make_number(int value);

/**
 * Creates a string.
 * Example: "You're only resorting to physical abuse because you can't prove that I'm wrong. ~ Armin"
 */
ASTNode *ast_make_string(char *value);

/**
 * Creates a symbol.
 * Symbols can be seen as variables for the most part.
 * Example: otonashi = new Person
 *          ^
 */
ASTNode *ast_make_symbol(char *symbol);

/**
 * Makes an operator. Which is more like an infix function call.
 * Examples: +, -, /, *, etc.
 */
ASTNode *ast_make_op(char *op, ASTNode *l, ASTNode *r);
