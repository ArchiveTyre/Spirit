#pragma once

/*
 * You can access the global symbol table by accessing ast_root_node->symentry.
 */

typedef struct SymbolTableEntry SymbolTableEntry;
struct SymbolTableEntry {

	char *symbol_name;
	char *symbol_type;
	SymbolTableEntry *next_sibling;
	SymbolTableEntry *first_child;
	SymbolTableEntry *parent_table;
};

//SymbolTableEntry *global_symbol_table;
SymbolTableEntry *newest_symbol_table;

/**
 * Defines one symbol.
 */
SymbolTableEntry *sym_define(char *symbol_name, char *symbol_type, SymbolTableEntry *parent);

/**
 * Finds one symbol from perspective.
 */
SymbolTableEntry *sym_find(char *symbol_name, SymbolTableEntry *perspective);

/**
 * Frees a node. Baka.
 */
 void free_sym(SymbolTableEntry *node);

//SymbolTableEntry *sym_name_mangle
