#include "types.h"
#include <assert.h>
void init_types()
{
	assert(global_symbol_table != NULL);

	// FIXME: Is this really necessary?
	TYPE_VAR = sym_define ("var", NULL, SYM_TYPE, global_symbol_table);

	TYPE_FUN = sym_define("fun", NULL, SYM_TYPE, global_symbol_table);
	TYPE_INT = sym_define("int", NULL, SYM_TYPE, global_symbol_table);
	TYPE_STRING = sym_define("string", NULL, SYM_TYPE, global_symbol_table);
	TYPE_CLASS = sym_define("class", NULL, SYM_TYPE, global_symbol_table);
	TYPE_TUPLE = sym_define("tuple", NULL, SYM_TYPE, global_symbol_table);
	TYPE_VOID = sym_define("void", NULL, SYM_TYPE, global_symbol_table);

	/* TODO: Move this somewhere else. */
	sym_define("new", TYPE_FUN, SYM_NONE, global_symbol_table);
	sym_define("import", TYPE_FUN, SYM_NONE, global_symbol_table);
	sym_define("=", TYPE_FUN, SYM_NONE, global_symbol_table);
	sym_define("+", TYPE_FUN, SYM_NONE, global_symbol_table);
	sym_define("-", TYPE_FUN, SYM_NONE, global_symbol_table);
	sym_define(".", TYPE_FUN, SYM_NONE, global_symbol_table);

}
