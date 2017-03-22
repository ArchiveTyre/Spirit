#pragma once
#include "ASTBase.hpp"

class ASTNumber : public ASTBase {
	public:

		/*** MEMBER VARIABLES ***/
		int value;

		/*** METHODS ***/
		bool compileToBackend(ClassCompile *compile_dest) override;
		void debugSelf() override;
};