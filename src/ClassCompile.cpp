#include "ClassCompile.hpp"
#include <fstream>
#include <cstring>
#include <iostream>
#include <sys/stat.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <libgen.h>
#include "AST/ASTWithArgs.hpp"
#include "Parser.hpp"
#include "Lexer.hpp"
#include "Version.hpp"

using std::string;
using std::cout;
using std::endl;

string ClassCompile::default_out_dir = "out";
ClassCompile *ClassCompile::active_compilation = nullptr;
ASTClass ClassCompile::root_class(nullptr, "_root");

/*** HELPER FUNCTIONS ***/

static int mkpath(char *dir, mode_t mode)
{
    struct stat sb;

    if (!dir) {
		printf("ERROR: Can't mkpath on null.");
		abort();
        return 1;
    }

	/* Check if already done. */
    if (!stat(dir, &sb))
        return 0;

	char *dir_name = (char*)alloca(strlen(dir) + 1);
	strcpy(dir_name, dir);
    mkpath(dirname(dir_name), mode);

    return mkdir(dir, mode);
}

/*** IMPLEMENTATION ***/

bool ClassCompile::needsRecompile()
{
	struct stat src_stat, backend_out_stat;

	/* If src file does not exist we have a problem in the compiler code. */
    if (stat(this->in_file_path.c_str(), &src_stat) != 0) {
		cout << "ERROR: Could not get stat for src file: " <<  this->in_file_path << endl;
		abort();
        return 1;
    }

	/* If backend out file does not exist then clearly we have to recompile. */
	else if (stat(out_file_path.c_str(), &backend_out_stat) != 0) {
		return true;
	}

	/* Return the value of the check that src is newer than out. */
    return difftime(src_stat.st_mtime, backend_out_stat.st_mtime) >= 0;
}

bool ClassCompile::compileFile () {
	
	ClassCompile *suspended_compilation = ClassCompile::active_compilation;
	active_compilation = this;
	
	cout << "[INFO] Compiling class: " << class_name << " to: " << out_file_path <<endl;

	
	/* Test if we should compile from source. */
	if (needsRecompile()) {

		/** PARSE THE FILE INTO AST **/

		/* Parse. */
		std::ifstream input(this->in_file_path);
		Lexer lexer(&input, this->in_file_path);
		Parser parser(lexer);
		parser.parseInput(this->class_ast);

		/* Compile the file. */


		/** COMPILE THE FILE **/

		this->class_ast->compileToBackend(this);
		this->class_ast->compileToBackendHeader(this);
		this->class_ast->debugSelf();


		/** SAVE THE OUTPUT **/

		/* Assure the existance of the out dir. */
		char *dir_name = (char *)alloca(out_file_path.length() + 1);
		strcpy(dir_name, out_file_path.c_str());
		mkpath(dirname(dir_name), 0777);


		/* Open the file for writing. */

		std::ofstream file(out_file_path);
		if (file.is_open()) {
			file << "#include " << '"' << std::string(class_name).append(".ch.hpp") << '"' << std::endl;
			file << this->output_stream.rdbuf();
			file << "/* Compiled with: " << Version::name << " v" << Version::version << "*/" << std::endl;
			file.flush();
			file.close();
		}
		else {
			cout << "ERROR: Could not open file: " << out_file_path << std::endl;
		}
		
		std::ofstream header_file(out_header_file_path);
		if (header_file.is_open()) {
			header_file << "#pragma once" << std::endl;
			header_file << this->output_header_stream.rdbuf();
			header_file << "/* Compiled with: " << Version::name << " v" << Version::version << "*/" << std::endl;
			header_file.flush();
			header_file.close();
		}
		else {
			cout << "ERROR: Could not open file: " << out_header_file_path << std::endl;
		}
	}

	/* Or just load from cached symbol table. */
	else {

		cout << "[INFO] Class already compiled!" << endl;

		/* Cached symbols are saved in .ch.sym files. */
		std::ifstream file(string(out_file_path).append(".sym"));
		ASTBase::importSymFromStream(static_cast<ASTBase*>(static_cast<ASTNamed*>(class_ast)), file);
	}

	/* Continue old compilation. */
	active_compilation = suspended_compilation;

	return true;
}

/* Extract class name from file_path arg and set it.*/
static string get_class_name(std::string file_path)
{
	char *old_path = strdup(file_path.c_str());
	string file_name = string(basename(old_path));
	string class_name = file_name.substr(0, file_name.find_first_of('.'));
	free(old_path);
	return class_name;
}

ClassCompile::ClassCompile(std::string file_path)
{

	class_ast = new ASTClass(dynamic_cast<ASTBlock*>(&root_class), get_class_name(file_path));
	
	/* Set the class name to the ASTClass's class name. */
	this->class_name = this->class_ast->ast_name;

	/* Set out dir to default out dir. */
	this->out_dir = ClassCompile::default_out_dir;

	
	this->out_header_file_path = string(out_dir).append(1, '/').append(file_path).append(".hpp");
	
	this->out_file_path = string(out_dir).append(1, '/').append(file_path).append(".cpp");
	
	this->in_file_path = file_path;
}
