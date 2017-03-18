#include "symtbl.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

SymbolTableEntry *sym_define(char *symbol_name, char *symbol_type, SymbolTableEntry *parent)
{
	SymbolTableEntry *target = malloc(sizeof(SymbolTableEntry));
	target->symbol_name = strdup(symbol_name);
	target->symbol_type = strdup(symbol_type);
	target->first_child = NULL;
	target->next_sibling = NULL;
	target->symbol_info = NULL;
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

void sym_add_info(SymbolTableEntry *sym, char *info)
{
#ifdef DEBUG
	if (info == NULL) {
		printf("ERROR: info is null!\n");
		abort();
		return;
	}
#endif
	SymbolInfo *new_info = malloc(sizeof(SymbolInfo));
	new_info->info_no = 0;
	new_info->info_text = strdup(info);
	new_info->info_sibling = NULL;

	/* Find a siblingless symbol_info to place self into. */
	SymbolInfo **dest = &sym->symbol_info;
	while (*dest != NULL)
		dest = &(*dest)->info_sibling;
	*dest = new_info;
	new_info->info_no = (*dest)->info_no + 1;

}

SymbolTableEntry *sym_find(char *symbol_name, SymbolTableEntry *perspective)
{
	// FIXME: Use a hashmap or something.
	if (perspective == NULL) {
		printf("ERROR: perspective is null!\n");
		abort();
	}
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
	if (node->first_child != NULL)
		free_sym(node->first_child);
	if (node->next_sibling != NULL)
		free_sym(node->next_sibling);

	/* Iterate through symbol info and free them. */
	SymbolInfo *symbol_info_iterator = node->symbol_info;
	while(symbol_info_iterator != NULL) {
		SymbolInfo *next = symbol_info_iterator->info_sibling;
		free(symbol_info_iterator->info_text);
		free(symbol_info_iterator);
		symbol_info_iterator = next;
	}

	free(node->symbol_name);
	free(node->symbol_type);
	free(node);
}
