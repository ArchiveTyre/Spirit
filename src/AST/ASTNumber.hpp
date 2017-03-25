#pragma once
#ifndef AST_NUMBER_HPP
#define AST_NUMBER_HPP

#include "ASTBase.hpp"

class ASTNumber : public ASTBase {
public:

	/*** MEMBER VARIABLES ***/
	/* The integer value of the number. */
	int value;

	/*** METHODS ***/
	ASTNumber(int value);
	bool compileToBackend(ClassCompile *compile_dest) override;
	void debugSelf() override;
};

#endif
