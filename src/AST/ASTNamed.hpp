/**
 * @class ASTNamed
 * 
 * Abstract class for ASTs with a name.
 * 
 * @author Tyrerexus
 * @date March 25 2017
 */

#pragma once
#ifndef AST_NAMED_HPP
#define AST_NAMED_HPP

#include <string>
#include "ASTBase.hpp"

class ASTNamed : virtual public ASTBase {
public:
	
	/** The name of this AST. */
	std::string ast_name;
	
	/** Creates a named AST by name 
	 *
	 */
	ASTNamed(std::string new_name);
	
	virtual void exportSymToStream(std::ostream& output) override;
	virtual bool compileToBackend(ClassCompile *compile_dest) override;
	virtual void debugSelf() override;
};

#endif
