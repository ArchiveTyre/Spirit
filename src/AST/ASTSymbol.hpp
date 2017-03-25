#pragma once
#ifndef AST_SYMBOL_HPP
#define AST_SYMBOL_HPP

#include "ASTNamed.hpp"

class ASTSymbol : public ASTNamed
{
public:
	ASTSymbol(std::string name);
};

#endif
