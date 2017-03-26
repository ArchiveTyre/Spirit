/**
 * @class ASTNumber
 * 
 * Defines a number as an AST
 * 
 * @author Tyrerexus
 * @date 25 March 2017
 */

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
