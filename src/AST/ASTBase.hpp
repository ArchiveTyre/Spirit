#pragma once
#ifndef AST_BASE_HPP
#define AST_BASE_HPP

#include <stack>
#include <iostream>

/* ClassCompile will be defined later. */
class ClassCompile;

class ASTBase {
public:

	/*** METHODS ***/
	static void importSymFromStream(ASTBase *dest, std::istream& input);
	
	virtual bool compileToBackend(ClassCompile *compile_dest) = 0;
	virtual void exportSymToStream(std::ostream& output);
	virtual bool compileToBackendHeader(ClassCompile *compile_dest);
	virtual void debugSelf();
	
	ASTBase();	

	/*** MEMBER VARIABLES ***/
	ASTBase *parent_node;
	int line_no;
	int indentation_level;
};
#endif
