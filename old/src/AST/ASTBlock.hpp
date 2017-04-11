/**
 * @class ASTBlock
 * 
 * An ASTBlock contains child ASTs. 
 * They also act as scopes.
 * 
 * @author Tyrereuxs
 * @date 25 March 2017
 */

#pragma once
#ifndef AST_BLOCK_HPP
#define AST_BLOCK_HPP

#include <list>
#include "ASTBase.hpp"

class ASTBlock : public ASTBase {
public:
	
	friend class ASTBase;
	
	/*** METHODS ***/
	
	ASTBlock(ASTBase *parent);
	~ASTBlock();
	
	
	/*** OVERRIDES ***/
	
	virtual bool compileToBackend(ClassCompile *compile_dest) override;	
	virtual bool compileToBackendHeader(ClassCompile *compile_dest) override;
	virtual void debugSelf() override;

	
	/*** MEMBER VARIABLES ***/
	
	/** All nodes contained in this block */
	std::list<ASTBase *> child_nodes;
};

#endif
