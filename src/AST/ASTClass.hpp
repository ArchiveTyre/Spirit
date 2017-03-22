#pragma once
#include <string>
#include "ASTNamed.hpp"
#include "ASTBlock.hpp"

class ASTClass : virtual public ASTNamed, virtual public ASTBlock {
	public:
		ASTClass(std::string class_name);
		bool compileToBackend(ClassCompile *compile_dest) override;
		void exportSymToStream(std::ostream& output) override;
};
