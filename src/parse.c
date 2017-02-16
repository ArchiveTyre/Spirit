#include "parse.h"

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#include <ast_node.h>

/* See header file for documentation. */

/** The top level AST node created in parse_file. */
static ASTNode *root_node = NULL;

/** The previously parsed AST node. */
static ASTNode *prev_node;

void parse_file(char *filename)
{
	FILE *file_to_parse = fopen(filename, "r");
	char *line_to_parse = NULL;

	/* Make sure that the root node exists. */
	// FIXME: Should every file perhaps have it's own root_node?
	if (root_node == NULL)
		root_node = new_ast_node("root", "", -1);

	prev_node = root_node;

	/* Read the file line-by-line. */
	while(true) {
		size_t length;
		size_t read = getline(&line_to_parse, 
				&length, 
				file_to_parse); 
		if (read == -1) {
			break;
		}
		parse_line(line_to_parse);
	}

	debug_ast_node(root_node, 0);
}

/**
 * Finds out the indentation,
 * command and argumnts of one line.
 */
static void parse_command_info(char *line, 
			int *indentation, 
			char **command_name, 
			char **command_args)
{

	/* How many tabs there are at the begining. */
	int indent = 0;

	/* Check how large the indent. */
	for(char *check = line; *check == '\t'; check++)
		indent++;

	*indentation = indent;

	/* For how long until the first word is interrupted*/
	int first_word_size = 0;

	/* Begin scanning after the indentation. */
	for(char *check = line+indent; *check != ' ' && 
					*check !='\n' && 
					*check != 0; check++ )
		first_word_size++;	

	*command_name = malloc(first_word_size);
	memcpy(*command_name, line+indent, first_word_size);

	/* Plus one because we skip the space. */
	char* remaining_str = line+indent+first_word_size+1;
	size_t remaining_str_length = strlen(remaining_str);

	/* 
	 * If there are no args than we should set command_args
	 * to null even if it probably already is null.
	 */
	if (remaining_str_length == 0) {
		*command_args = NULL;
	}
	else {
		/* Plus one so that we can set a null terminator*/
		*command_args = malloc(remaining_str_length + 1);
		memcpy(*command_args, remaining_str, remaining_str_length - 1);
		(*command_args)[remaining_str_length - 1] = '\0';
	}
}

void parse_line(char *line)
{

	/* No point in compiling empty lines... */
	if (strlen(line) == 0)
		return;

	/* 
	 * Parse one line and store the result for creating
	 * the new AST node for this line. 
	 */
	int indentation;
	char *command_name = NULL;
	char *command_args = NULL;
	parse_command_info(line, 
			   &indentation, 
			   &command_name, 
			   &command_args);	

	/* No point in compiling lines with no meaning. */
	if (strlen(command_name) == 0)
		return;
	
	ASTNode *node = new_ast_node(command_name, command_args, indentation);

	/* Decide where to put this AST node. */

	/* Check if we should descend. */
	if (indentation > prev_node->indentation) {
		ast_node_insert(prev_node, node);
	}

	/* Check if we should stay on the same level. */
	else if (indentation == prev_node->indentation) {
		ast_node_insert(prev_node->parent_node, node);
	}

	/* Check if we should ascend. */
	else  {
		ASTNode *new_parent = prev_node->parent_node;
		while (node->indentation <= new_parent->indentation) {
			new_parent = new_parent->parent_node;
		}
		ast_node_insert(new_parent, node);
	}

	prev_node = node;
}
