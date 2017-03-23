#pragma once
#include <string>
#include <sstream>
#include "AST/ASTClass.hpp"

class ClassCompile {
	public:
		
		static ClassCompile *active_compilation;

		/*** METHODS ***/
		bool compileFile();
		ClassCompile(std::string file_path);

		/*** MEBMER VARIABLES ***/
		std::string out_dir;
		std::ostringstream output_stream;
		std::ostringstream output_header_stream;

		std::string class_name;
		std::string out_file_path;
		std::string out_header_file_path;
		std::string in_file_path;


		ASTClass class_ast;

		/*** STATICS ***/
		static std::string default_out_dir;

	private:
		bool needsRecompile();
};
