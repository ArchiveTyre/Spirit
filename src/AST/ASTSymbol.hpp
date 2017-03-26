/**
 * @class ASTSymbol
 * 
 * A symbol has a name and a reference to 
 * it's declaration
 * 
 * @author Tyrerexus
 * @date 25 March 2017
 */

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
