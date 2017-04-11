/**
 * @class ASTDefineVariable
 * 
 * A definition of a variable.
 * 
 * @author Tyrerexus
 * @date 28 Match 2017
 */

#pragma once
#ifndef AST_DEFINE_VARIABLE_HPP
#define AST_DEFINE_VARIABLE_HPP

#include "ASTNamed.hpp"
#include "ASTBase.hpp"
#include "../ClassCompile.hpp"

class ASTDefineVariable : public ASTNamed {
public:
	
	/*** MEMBER VARIABLES ***/
	
	ASTBase *initial_value;
	ASTType *ast_type;
	
	
	/*** METHODS ***/
	
	ASTDefineVariable(ASTBase *parent, std::string name, ASTBase *initial_value);
	
	
	/*** OVERRIDES ***/
	
	virtual void exportSymToStream(std::ostream& output) override;
	virtual bool compileToBackend(ClassCompile *compile_dest) override;
	virtual void debugSelf() override;
	bool compileToBackendHeader(ClassCompile *compile_dest) override;
	void confirmParent() override;
	
};

#endif
