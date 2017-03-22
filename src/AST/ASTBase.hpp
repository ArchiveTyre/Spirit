#pragma once
#include <stack>
#include <iostream>

/* ClassCompile will be defined later. */
class ClassCompile;

class ASTBase {
	public:

		/*** METHODS ***/
		static void importSymFromStream(ASTBase *dest, std::istream& input);
		
		virtual void exportSymToStream(std::ostream& output);
		virtual bool compileToBackend(ClassCompile *compile_dest) = 0;
		virtual void debugSelf();
		
		ASTBase();	

		/*** MEMBER VARIABLES ***/
		ASTBase *parent_node;
		int line_no;
		int indentation_level;
};