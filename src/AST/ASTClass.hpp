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
	 * @param class_name The name of the class.
	 */
	ASTClass(std::string class_name);
	
	/** Automatically finds the correct place for new AST nodes in this class.
	 * @param new_code The new AST node that should be inserted.
	 */
	void insertNewCode(ASTBase *new_code);
	
	bool compileToBackend(ClassCompile *compile_dest) override;
	bool compileToBackendHeader(ClassCompile *compile_dest) override;
	void debugSelf() override;
	void exportSymToStream(std::ostream& output) override;
	
private:
	
	/*** MEMBER VARIABLES ***/
	
	/** The node that was inserted last time by insertNewCode() */
	ASTBase *newly_inserted_node;
};

#endif
