/**
 * @class ASTClass
 * 
 * Defines a simple class in the AST.
 * 
 * @authors Tyrerexus
 * @date 25 March, 2017
 */

#pragma once
#ifndef AST_CLASS_HPP
#define AST_CLASS_HPP

#include <string>
#include "ASTNamed.hpp"
#include "ASTBlock.hpp"

class ASTClass : virtual public ASTNamed, virtual public ASTBlock {
public:
	
	/*** METHODS ***/
	
	/** Defines a class by name
	 * @param parent The parent of this class.
	 * @param class_name The name of the class.
	 */
	ASTClass(ASTBase *parent, std::string class_name);
	
	/** Automatically finds the correct place for new AST nodes in this class.
	 * @param line_indent The indentation of the code.
	 * @return The parent for the new code.
	 */
	ASTBase * getParentForNewCode(int line_indent);
	
	
	/*** MEMBERS ***/
	
	bool compileToBackend(ClassCompile *compile_dest) override;
	bool compileToBackendHeader(ClassCompile *compile_dest) override;
	void debugSelf() override;
	void exportSymToStream(std::ostream& output) override;
};

#endif
