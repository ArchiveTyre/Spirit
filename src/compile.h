#pragma once
#include <stdbool.h>

typedef struct CompileResult CompileResult;
struct CompileResult {
	char *file_name;
};

bool compile_file(char *file_name, bool include_only);
