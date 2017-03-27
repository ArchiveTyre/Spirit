/**
 * @class ClassCompile
 * 
 * A class for compiling ASTClasses.
 * 
 * @author Tyrerexus
 * @date 26 March 2017
 */

#pragma once
#ifndef CLASS_COMPILE_HPP
#define CLASS_COMPILE_HPP

#include <string>
#include <sstream>
#include "AST/ASTClass.hpp"

class ClassCompile {
public:

	/*** METHODS ***/
	
	/** Do the compilation of this ClassCompile.
	 * @return Returns true on success.
	 */
	bool compileFile();
	
	/** Creates a ClassCompile based on a path.
	 * @param file_path The path to the file that should be compiled.
	 */
	ClassCompile(std::string file_path);

	/*** MEBMER VARIABLES ***/
	
	/** The output directory. */
	std::string out_dir;
	
	/** The opened stream for the backend generated file. */
	std::ostringstream output_stream;
	
	/** The opened stream for the backend header generated file. */
	std::ostringstream output_header_stream;

	/** The class name. */
	std::string class_name;
	
	/** The path to the file that we will open as a stream later on with. */
	std::string out_file_path;
	
	/**The path to the header file that we will open as a stream later on with. */
	std::string out_header_file_path;
	
	/** From where to read from. */
	std::string in_file_path;

	/** The class AST for the compilation. */
	ASTClass class_ast;

	/*** STATICS ***/
	
	/** FIXME: Now with our own parser this should be removed. The kinds of global variables are EVIL! */
	static ClassCompile *active_compilation;
	
	/** The default output dir. */
	static std::string default_out_dir;

private:
	
	/** Checks if recompile is needed.
	 * @return Returns true if recompile is needed.
	 */
	bool needsRecompile();
};

#endif
