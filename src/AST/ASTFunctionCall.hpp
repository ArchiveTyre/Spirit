/**
 * @class ASTFunctionCall
 * 
 * Defines a function call to another function.
 * 
 * @author Tyrerexus
 * @date 25 March 2017
 */

#pragma once
#ifndef AST_FUNCTION_CALL_HPP
#define AST_FUNCTION_CALL_HPP

#include "ASTNamed.hpp"
#include "ASTWithArgs.hpp"
#include "../ClassCompile.hpp"

class ASTFunctionCall : virtual public ASTNamed, virtual public ASTWithArgs {
public:

	/*** METHODS ***/
	
	/** Creates a function call to an AST node.
	 * @param function_name The name of the function that this AST wants to call.
	 */
	ASTFunctionCall(ASTBase *parent, std::string function_name);
	virtual ~ASTFunctionCall();
	
	/*** OVERRIDES ***/
	
	bool compileToBackend(ClassCompile *compile_dest) override;
	bool compileToBackendHeader(ClassCompile *compile_dest) override;
	void debugSelf() override;
	void exportSymToStream(std::ostream& output) override;
	virtual ASTType * getExpressionType() override;
	

	/*** MEMBER VARIABLES ***/
	bool is_infix = false;
	
	
protected:
	
	virtual ASTBase * findSymbolScan(std::string name) override;
};		

#endif
