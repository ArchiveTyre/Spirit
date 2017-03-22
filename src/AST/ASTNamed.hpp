#pragma once
#include <string>
#include "ASTBase.hpp"

/**
 * Defines an AST node that has a name.
 */
class ASTNamed : virtual public ASTBase {
	public:
		std::string ast_name;
		virtual void exportSymToStream(std::ostream& output) override;
		virtual bool compileToBackend(ClassCompile *compile_dest) override;
        virtual void debugSelf() override;
};
