#pragma once

typedef enum {
	AST_NONE,
	AST_SYMBOL,
	AST_FUNCTION_CALL,
	AST_FUNCTION_ARG,
	AST_OPERATION,
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

	/* For AST_BLOCK. */
	char *block_type;
	ASTNode *parent_node;

	/* For AST_OPERATION. */
	ASTNode *left_ast;
	ASTNode *right_ast;
	char *op_name;

	/* For AST_SYMBOL. */
	char *symbol_name;

	/* For AST_FUNCTION_CALL and AST_BLOCK. */
	ASTNode *args_chain;

	/* For AST_FUNCTION_CALL. */
	char *function_name;

	/* For AST_NUMBER. */
	int number_value;

	/* For AST_STRING*/
	char *string_value;


};

ASTNode *ast_root_node;
ASTNode *ast_prev_node;
void ast_insert_node(ASTNode *node);

void debug_ast_node(ASTNode *node, int indent);
void free_ast_node(ASTNode *target);

ASTNode *ast_make_func_call(char *function_name);
ASTNode *ast_make_number(int value);
ASTNode *ast_make_symbol(char *symbol);
ASTNode *ast_make_string(char *value);
ASTNode *ast_make_op(char *op, ASTNode *l, ASTNode *r);
ASTNode *ast_make_block(char *block_type);
ASTNode *ast_make_root();
ASTNode *ast_make_default_arg();
