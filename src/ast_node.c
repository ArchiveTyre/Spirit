#include "ast_node.h"

#include <stdlib.h>
#include <stdio.h>

/* See header file for documentation. */

void free_ast_node(ASTNode *this)
{
	if (this->first_child != NULL)
		free_ast_node(this->first_child);
	if (this->next_node != NULL)
		free_ast_node(this->next_node);
	free(this->args);
	free(this->command);
}

ASTNode* new_ast_node(char *command, char *args, int indentation)
{
	ASTNode *this = malloc(sizeof(ASTNode));
	this->parent_node = NULL;
	this->next_node = NULL;
	this->first_child = NULL;
	this->args = args;
	this->command = command;
	this->indentation = indentation;
	return this;
}

void ast_node_insert(ASTNode *this, ASTNode *node)
{
	node->parent_node = this;
	if (this->first_child == NULL) {
		this->first_child = node;
	}
	else {
		ASTNode **empty = &(this->first_child->next_node);
		while (*empty != NULL) 
			empty = &(*empty)->next_node;
		*empty = node;
	}
}

void debug_ast_node(ASTNode *this, int indentation)
{
	for (int i = 0; i < indentation; i++) putchar('\t');
	printf("{Com: %s, Arg: %s, Ind: %d}\n", this->command, this->args, this->indentation);

	if (this->first_child != NULL)
		debug_ast_node(this->first_child, indentation+1);
	if (this->next_node != NULL)
		debug_ast_node(this->next_node, indentation);
}
