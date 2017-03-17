#pragma once
#include <stdbool.h>
#include <stdio.h>
#include "symtbl.h"
#include "ast.h"

typedef struct CompileResult CompileResult;
struct CompileResult {
	char *file_name;
	char *class_name;
	bool success;
	SymbolTableEntry *sym_entry;
	ASTNode *ast_root_node;
	ASTNode *ast_prev_node;

	FILE* out_file;
	char *out_file_name;
};

CompileResult *current_compile_result;

CompileResult *compile_file(char *file_name);
void free_compile_result(CompileResult *target);

const char *out_dir;
FILE *out_file;
