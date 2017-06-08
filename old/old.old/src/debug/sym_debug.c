#include "sym_debug.h"

void debug_symbol(const SymbolTableEntry *entry, int indent)
{
	if (entry == NULL)
		return;

	printf("%d", indent);
	for(int i = 0; i <= indent; i++)
		putchar('\t');

	char *parent_symbol_name = entry->parent_table != NULL ? entry->parent_table->symbol_name : "NONE";

	printf("%s (Type:%d, Parent:%s)\n", entry->symbol_name, entry->symbol_type_type, parent_symbol_name);
	debug_symbol(entry->first_child, indent+1);
	debug_symbol(entry->next_sibling, indent);
}
