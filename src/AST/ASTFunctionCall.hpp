#pragma once
#ifndef AST_FUNCTION_CALL_HPP
#define AST_FUNCTION_CALL_HPP

#include "ASTNamed.hpp"
#include "ASTWithArgs.hpp"
#include "../ClassCompile.hpp"

class ASTFunctionCall : virtual public ASTNamed, virtual public ASTWithArgs {
	public:

		/*** METHODS ***/
		ASTFunctionCall(std::string function_name);
		bool compileToBackend(ClassCompile *compile_dest) override;
		void debugSelf() override;
		void exportSymToStream(std::ostream& output) override;

		/*** MEMBER VARIABLES ***/
		bool is_infix = false;
};		

#endif
