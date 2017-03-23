#pragma once
#include "ASTBase.hpp"

class ASTNumber : public ASTBase {
	public:

		/*** MEMBER VARIABLES ***/
		int value;

		/*** METHODS ***/
		ASTNumber(int value);
		bool compileToBackend(ClassCompile *compile_dest) override;
		void debugSelf() override;
};
