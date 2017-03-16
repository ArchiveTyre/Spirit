#pragma once
#include "../ast.h"

/**
 * Prints out the AST given in a tree format.
 */
void debug_ast_node(ASTNode *node, bool in_expr, bool do_next, int indent);
