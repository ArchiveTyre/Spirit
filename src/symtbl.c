#include "symtbl.h"
#include <stdlib.h>
#include <string.h>

SymbolTableEntry *sym_define(char *symbol_name, char *symbol_type, SymbolTableEntry *parent)
{
	SymbolTableEntry *target = malloc(sizeof(SymbolTableEntry));
	target->symbol_name = strdup(symbol_name);
	target->symbol_type = strdup(symbol_type);
	target->first_child = NULL;
	target->next_sibling = NULL;
	target->parent_table = parent;

	/* If there's no parent we can't put the target into the parent's table. */
	if (parent != NULL) {

		/* Put target under the parent table. */
		SymbolTableEntry **dest = &parent->first_child;
		while (*dest != NULL)
			dest = &(*dest)->next_sibling;
		*dest = target;
	}

	newest_symbol_table = target;

	return target;
}

SymbolTableEntry *sym_find(char *symbol_name, SymbolTableEntry *perspective)
{
	// FIXME: Use a hashmap or something.

	SymbolTableEntry *search = perspective->first_child;
	while (search != NULL) {
		if (strcmp(search->symbol_name, symbol_name) == 0)
			return search;
		search = search->next_sibling;
	}

	if (perspective->parent_table != NULL)
		return sym_find(symbol_name, perspective->parent_table);
	return NULL;
}

void free_sym(SymbolTableEntry *node)
{
	free(node->symbol_name);
	free(node->symbol_type);
	if (node->first_child != NULL)
		free(node->first_child);
	if (node->next_sibling != NULL)
		free(node->next_sibling);
	free(node);
}
