/**
 * @class Builtins
 * 
 * Defines builtin types, variables, functions into the global AST.
 * Call install_types() to install the types. Otherwise the compiler will fail.
 */

#pragma once
#ifndef BUILTINS_HPP
#define BUILTINS_HPP

#include "AST/ASTType.hpp"

class Builtins {
public:
	
	static ASTType *type_integer;
	static ASTType *type_string;
	static ASTType *type_function;
	
	/** Installs the builtins.
	 */
	static void install_types();
};

#endif
