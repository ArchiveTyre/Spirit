#pragma once
#include "symtbl.h"

const SymbolTableEntry *TYPE_VAR;
const SymbolTableEntry *TYPE_FUN;
const SymbolTableEntry *TYPE_INT;
const SymbolTableEntry *TYPE_STRING;
const SymbolTableEntry *TYPE_CLASS;
const SymbolTableEntry *TYPE_TUPLE;
const SymbolTableEntry *TYPE_VOID;

void init_types();
