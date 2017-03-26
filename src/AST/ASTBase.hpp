/**
 * @class ASTBase
 * 
 * The base class of all ASTs used by the compiler.
 * 
 * @author Tyrerexus
 * @date 25 March 2017
 */

#pragma once
#ifndef AST_BASE_HPP
#define AST_BASE_HPP

#include <stack>
#include <iostream>

/* ClassCompile will be defined later. */
class ClassCompile;

class ASTBase {
public:

	/*** METHODS ***/
	
	/** Imports an AST from a symbol file.
	 * @param dest Where to import to.
	 * @param input From where.
	 */
	static void importSymFromStream(ASTBase *dest, std::istream& input);
	
	/** Exports this AST into a symbol file.
	 * @param output The "symbol file" stream.
	 */
	virtual void exportSymToStream(std::ostream& output);
	
	/** Compiles this node into the desired backend.
	 * @param compile_dest ClassCompile contains members that tell us where to put our results.
	 * @return Returns true if compilation of this node succeds.
	 */
	virtual bool compileToBackend(ClassCompile *compile_dest) = 0;
	
	/** Compiles this node into a header format for the desired backend.
	 * @param compile_dest ClassCompile contains members that tell us where to put our results.
	 * @return Returns true if compilation of this node succeds.
	 */
	virtual bool compileToBackendHeader(ClassCompile *compile_dest);
	
	/** Prints out self for debugging.
	 */
	virtual void debugSelf();
	
	/** Creates a basic AST node.
	 */
	ASTBase();	

	/*** MEMBER VARIABLES ***/
	
	/** The node that contains this node. */
	ASTBase *parent_node;
	
	/** On which line this node was defined. */
	int line_no;
	
	/** What indentation level this node has. */
	int indentation_level;
};
#endif
