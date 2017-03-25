#pragma once
#ifndef AST_BLOCK_HPP
#define AST_BLOCK_HPP

#include <list>
#include "ASTBase.hpp"

class ASTBlock : public ASTBase {
public:
	/*** METHODS ***/
	void insertChild(ASTBase *node);
	virtual bool compileToBackend(ClassCompile *compile_dest) override;
	virtual bool compileToBackendHeader(ClassCompile *compile_dest) override;
	virtual void debugSelf() override;

	/*** MEMBER VARIABLES ***/
	std::list<ASTBase *> child_nodes;
};

#endif
