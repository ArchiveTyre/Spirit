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

#include "ASTDefineVariable.hpp"
#include "ASTNamed.hpp"

class ASTSymbol : public ASTNamed
{
public:
	
	ASTDefineVariable *definition;
	
	/** Creates a symbol with a name.
	 * @param name The name of this symbol.
	 */
	ASTSymbol(ASTBase *parent, std::string name);
	
	/*** OVERRIDES ***/
	
	virtual ASTType * getExpressionType();
};

#endif
