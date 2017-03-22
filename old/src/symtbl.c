#include "symtbl.h"
#include "types.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

SymbolTableEntry *global_symbol_table = NULL;

SymbolTableEntry *sym_define(char *symbol_name, const SymbolTableEntry *symbol_type, ESymType type_type, SymbolTableEntry *parent)
{
	printf("Defineing: %s\n", symbol_name);
	SymbolTableEntry *target = malloc(sizeof(SymbolTableEntry));
	target->symbol_name = strdup(symbol_name);
	target->symbol_type = symbol_type;
	target->symbol_type_type = type_type;
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
	printf("Finding: %s\n", symbol_name);

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

void free_sym(const SymbolTableEntry *node)
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
	if (node->symbol_type != NULL)
		free_sym(node->symbol_type);
	free((SymbolTableEntry*)node);
}

SymbolTableEntry *sym_load_from_file(FILE *file)
{
	int r;
	int line_no = 0;
	SymbolTableEntry *target = sym_define("", TYPE_CLASS, SYM_TYPE, NULL);
	SymbolTableEntry *parsing;

    for(;;) {
        line_no++;
		char *sym_type;
		char *sym_name;
		int parse_type;

		r = fscanf(file, "%d %ms %ms\n", &parse_type, &sym_type, &sym_name);
        if (r == 3) {

			switch (parse_type) {
				case 0:
					break;
				case 1:
					parsing = sym_define(sym_name, sym_find(sym_type, global_symbol_table), SYM_MEMBER, target);
					break;
				case 2:
					assert(parsing != NULL);
					sym_add_info(parsing, sym_name);
					break;
				default:
					printf ("ERROR: on line %d. Unknown parse type!\n", line_no);
			}

        }
		else if (r == EOF)
			break;
        else
            printf ("ERROR: on line %d. Corrupt file!\n", line_no);
		free(sym_type);
		free(sym_name);

    }

	return target;

}

void sym_save_to_file(SymbolTableEntry *symbol, FILE *file)
{

	/* Iterate through child symbols and save them. */
	SymbolTableEntry *iterator = symbol->first_child;
	while (iterator != NULL) {
		fprintf(file, "1 %s %s\n", iterator->symbol_type->symbol_name, iterator->symbol_name);

		/* Iterate through symbol info and save them. */
		SymbolInfo *symbol_info_iterator = iterator->symbol_info;
		while(symbol_info_iterator != NULL) {
			fprintf(file, "2 _info %s\n", symbol_info_iterator->info_text);
			symbol_info_iterator = symbol_info_iterator->info_sibling;
		}

		iterator = iterator->next_sibling;
	}
}
