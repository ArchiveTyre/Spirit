#pragma once
#ifndef BUILTINS_HPP
#define BUILTINS_HPP

#include "AST/ASTType.hpp"

class Builtins {
public:
	
	static ASTType type_integer;
	static ASTType type_string;
	static ASTType type_function;
	
	static void install_types();
};

#endif
