#pragma once
#include <string>
#include "ASTNamed.hpp"
#include "ASTBlock.hpp"

class ASTClass : virtual public ASTNamed, virtual public ASTBlock {
	public:
		
		/*** METHODS ***/
		ASTClass(std::string class_name);
		void insertNewCode(ASTBase *new_code);
		bool compileToBackend(ClassCompile *compile_dest) override;
		bool compileToBackendHeader(ClassCompile *compile_dest) override;
		void exportSymToStream(std::ostream& output) override;
		void debugSelf() override;
		
	private:
		
		/*** MEMBER VARIABLES ***/
		ASTBase *newly_inserted_node;
};
