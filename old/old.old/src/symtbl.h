#pragma once
#include <stdio.h>

/*
 * You can access the global symbol table by accessing current_compile_result->ast_root_node->symentry.
 */

typedef enum {
	SYM_NONE,
	SYM_MEMBER,
	SYM_TYPE,
	SYM_PACKAGE,
} ESymType;

typedef struct SymbolInfo SymbolInfo;
struct SymbolInfo {
	int info_no;
	char *info_text;
	SymbolInfo *info_sibling;
};

typedef struct SymbolTableEntry SymbolTableEntry;
struct SymbolTableEntry {

	/* The type of the type. */
	ESymType symbol_type_type;

	/* Symbol name. Example: FIXME*/
	char *symbol_name;

	/* The type. */
	//char *symbol_type;
	const SymbolTableEntry *symbol_type;

	SymbolTableEntry *next_sibling;
	SymbolTableEntry *first_child;
	SymbolTableEntry *parent_table;
	SymbolInfo *symbol_info;
};

SymbolTableEntry *newest_symbol_table;
SymbolTableEntry *global_symbol_table;

/**
 * Defines one symbol.
 */
SymbolTableEntry *sym_define(char *symbol_name, const SymbolTableEntry *symbol_type, ESymType type_type, SymbolTableEntry *parent);

void sym_add_info(SymbolTableEntry *sym, char *info);

/**
 * Finds one symbol from perspective.
 */
SymbolTableEntry *sym_find(char *symbol_name, SymbolTableEntry *perspective);

/**
 * Frees a node. Baka.
 */
 void free_sym(const SymbolTableEntry *node);

/**
 * Loads a symbol from a file.
 */
SymbolTableEntry *sym_load_from_file(FILE *file);

/**
 * Saves the symbol's children (no recursion) into a file.
 */
void sym_save_to_file(SymbolTableEntry *symbol, FILE *file);

//SymbolTableEntry *sym_name_mangle
